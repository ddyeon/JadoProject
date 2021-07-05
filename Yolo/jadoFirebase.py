# import the necessary packages
import datetime
from scipy.spatial import distance as dist
from imutils.video import VideoStream
from imutils import face_utils
import imutils
import dlib
import cv2
import numpy as np
import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

PATH_CRED = '/home/pi/iot/cred.json'
URL_DB = 'https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app/'
cameraCheck = 'false'

cred = credentials.Certificate(PATH_CRED)
firebase_admin.initialize_app(cred, {
   'databaseURL' : URL_DB
})

myref = db.reference(u'User').child(u'hyunji').child(u'Study')
now = datetime.datetime.now()



def getNowDate():
    nowDate = now.strftime('%Y-%m-%d')
    print(nowDate)  # 2015-04-19
    return nowDate


def get_write():
    myref.update({
        getNowDate(): {
            u'behavior_count': TIRED,
            u'behavior_time': string_time,

            u'total_time' : "",
            u'subjects' : {
                u'English' : "01:00:20",
                u'Korean' : "05:00:33",
                u'Math' : "03:34:11"
            }
        }
    })

# 앱에서 카메라 버튼 클릭 시 라즈베리파이 카메라 실행(firebase 이용)
def get_read():
    ref = db.reference('Camera')

    if ref.get() == 'true':
        global cameraCheck
        cameraCheck = 'true'

    print(ref.get())
    print('OK!')

# 눈 종횡비(세로) 계산 -> ear가 임계 값 이하일 경우 눈 감고 있는 것 으로 판단
def eye_aspect_ratio(eye):

    #눈에 랜드마크 좌표를 찍어서 EAR값을 예측
    #눈 세로 계산
    A = dist.euclidean(eye[1], eye[5])
    B = dist.euclidean(eye[2], eye[4])

    # 눈 가로 계산
    C = dist.euclidean(eye[0], eye[3])

    # 가로와 세로 이용하여 눈 종횡비 계산
    ear = (A + B) / (2.0 * C)

    # 종횡비 return
    return ear

# firebase 관련 함수 실행
get_read()


# 딴 짓 시간 (핸드폰)
global seconds, minutes, hours
seconds = int(0)
minutes, hours = int(0), int(0)

# 눈 감은 시간 - frame count (*count 초기화와 함께 초기화)
global seconds_dlib, minutes_dlib, hours_dlib
seconds_dlib = int(0)
minutes_dlib, hours_dlib = int(0), int(0)

# 딴 짓 시간 (졸음 - tired 플러스 된 시간)
global seconds_dlib_total, minutes_dlib_total, hours_dlib_total
seconds_dlib_total = int(0)
minutes_dlib_total, hours_dlib_total = int(0), int(0)

# 딴 짓 시간 최종 (핸드폰 +  졸음)
global seconds_total, minutes_total, hours_total
seconds_total = int(0)
minutes_total, hours_total = int(0), int(0)

# 딴 짓 한 횟수 (핸드폰)
global count_yolo
count_yolo = int(0)

# 전체 시간 배열
global string_time

# YOLO 가중치 파일과 CFG 파일 로드
YOLO_net = cv2.dnn.readNet("yolov3.weights","yolov3.cfg")

# YOLO NETWORK 재구성
classes = []
with open("coco.names", "r") as f:
    classes = [line.strip() for line in f.readlines()]
layer_names = YOLO_net.getLayerNames()
output_layers = [layer_names[i[0] - 1] for i in YOLO_net.getUnconnectedOutLayers()]

# 카메라가 켜져 있다면
if cameraCheck == 'true':

    # 눈의 임계값을 0.3으로 설정
    EYE_AR_THRESH = 0.3

    # 종횡비가 임계값보다 작은 5개의 연속 프레임 -> 눈 1회 감은 것으로 인식
    EYE_AR_CONSEC_FRAMES = 5

    # 프레임 카운터 초기화
    COUNTER = 0
    # 졸음 횟수 초기화
    TIRED = 0
    # 총 깜빡임 횟수 초기화
    TOTAL = 0

    # dlib를 이용하여 얼굴 감지 초기화 후에 face landmark 실행
    print("[INFO] loading facial landmark predictor...")
    detector = dlib.get_frontal_face_detector()
    predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')

    # 왼쪽, 오른쪽 face landmark의 index 설정
    (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
    (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]

    # video stream thread 시작
    print("[INFO] starting video stream thread...")
    fileStream = True
    VideoSignal = VideoStream(usePiCamera=True).start()
    # VideoSignal = cv2.VideoCapture(0)
    time.sleep(1.0)


    # video stream에서 프레임 반복
    while True:
        print("-------------시작-------------")
        # 웹캠 프레임
        frame = VideoSignal.read()
        h, w, c = frame.shape

        # YOLO 입력
        blob = cv2.dnn.blobFromImage(frame, 0.00392, (416, 416), (0, 0, 0),
        True, crop=False)
        YOLO_net.setInput(blob)
        outs = YOLO_net.forward(output_layers)

        class_ids = []
        confidences = []
        boxes = []

        for out in outs:
            for detection in out:
                scores = detection[5:]
                class_id = np.argmax(scores)
                confidence = scores[class_id]

                if confidence > 0.5:
                    # Object detected
                    center_x = int(detection[0] * w)
                    center_y = int(detection[1] * h)
                    dw = int(detection[2] * w)
                    dh = int(detection[3] * h)
                    # Rectangle coordinate
                    x = int(center_x - dw / 2)
                    y = int(center_y - dh / 2)
                    boxes.append([x, y, dw, dh])
                    confidences.append(float(confidence))
                    class_ids.append(class_id)
        indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.45, 0.4)

        if len(boxes) == 0:
            seconds = (seconds + 1)
            if seconds > 59:
                seconds = 0
                minutes = minutes + 1

            if minutes > 59:
                minutes = 0
                hours_ = hours + 1

            time.sleep(.1)
            print("            ", "자리 이탈 : ", '%02d' % hours, ":", '%02d' % minutes, ":", '%02d' % seconds)

        for i in range(len(boxes)):
            if i in indexes:
                x, y, w, h = boxes[i]
                label = str(classes[class_ids[i]])
                score = confidences[i]

                if label == "cell phone":
                    # 사람은 없는데 핸드폰만 있을 때
                    seconds = (seconds + 1)
                    if seconds > 59:
                        seconds = 0
                        minutes = minutes + 1

                    if minutes > 59:
                        minutes = 0
                        hours_ = hours + 1

                    time.sleep(.1)
                    print("핸드폰 한 시간 : ", '%02d' % hours, ":", '%02d' % minutes, ":", '%02d' % seconds)


                # 경계상자와 클래스 정보 이미지에 입력
                cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 5)
                cv2.putText(frame, label, (x, y - 20), cv2.FONT_ITALIC, 0.5,
                (255, 255, 255), 1)


        # 프레임 생성
        frame = imutils.resize(frame)
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # grayscale 프레임에서 얼굴 감지
        rects = detector(gray, 0)

        # 반복해서 얼굴 감지
        for rect in rects:
            # 설정된 얼굴 landmark의 (x,y) 좌표를 numpy로 변환
            shape = predictor(gray, rect)
            shape = face_utils.shape_to_np(shape)

            # 양쪽 눈 좌표 추출 후에 양쪽 눈의 종횡비 계산
            leftEye = shape[lStart:lEnd]
            rightEye = shape[rStart:rEnd]
            leftEAR = eye_aspect_ratio(leftEye)
            rightEAR = eye_aspect_ratio(rightEye)

            # 양쪽 눈 종횡비의 평균
            ear = (leftEAR + rightEAR) / 2.0

            # 양쪽 눈을 시각화
            leftEyeHull = cv2.convexHull(leftEye)
            rightEyeHull = cv2.convexHull(rightEye)
            cv2.drawContours(frame, [leftEyeHull], -1, (0, 255, 0), 1)
            cv2.drawContours(frame, [rightEyeHull], -1, (0, 255, 0), 1)

            # 눈 종횡비가 임계값 이하인지 확인
            # 눈 종회비가 임계값 보다 작으면 -> 깜빡임 카운트 증가
            if ear < EYE_AR_THRESH:
                COUNTER += 1
                print("1번 ear:" + str(ear) + "counter:" + str(COUNTER))
                cv2.putText(frame, "1번 ear:" + str(ear) + "counter:" + str(COUNTER), (10, 130),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

                seconds_dlib = (seconds_dlib + 1)
                if seconds_dlib > 59:
                    seconds_dlib = 0
                    minutes_dlib = minutes_dlib + 1

                if minutes_dlib > 59:
                    minutes_dlib = 0
                    hours_dlib = hours_dlib + 1

                time.sleep(.1)
                print("1번 : ", '%02d' % hours_dlib, ":", '%02d' % minutes_dlib, ":", '%02d' % seconds_dlib)

            # 눈 종회비가 임계값 보다 크면 -> 눈을 뜬 순간
            else:
                # 깜빡임 카운트가 프레임 보다 크면 -> 찐깜빡임
                if COUNTER >= EYE_AR_CONSEC_FRAMES:
                    TOTAL += 1
                    print("2번 ear:" + str(ear) + "total:" + str(TOTAL))
                    cv2.putText(frame, "2번 ear:" + str(ear) + "total:" + str(TOTAL), (10, 230),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

                # 깜빡임 카운트가 60보다 크면 -> 졸음이라고 인식
                if COUNTER >= 60:
                    TIRED += 1

                    print("3번 ear:" + str(ear) + "tired:" + str(TIRED))
                    cv2.putText(frame, "3번 ear:" + str(ear) + "tired:" + str(TIRED), (10, 330),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

                    print("Tired : ", '%02d' % hours_dlib, ":", '%02d' % minutes_dlib, ":", '%02d' % seconds_dlib)

                    if seconds_dlib_total > 59:
                        seconds_dlib_total = 0
                        minutes_dlib_total = minutes_dlib_total + 1

                    if minutes_dlib_total > 59:
                        minutes_dlib_total = 0
                        hours_dlib_total = hours_dlib_total + 1
                    seconds_dlib_total = seconds_dlib
                    minutes_dlib_total = minutes_dlib
                    hours_dlib_total = hours_dlib

                    print("딴 짓을 한 시간 : ", '%02d' % hours_dlib_total, ":", '%02d' % minutes_dlib_total, ":",
                          '%02d' % seconds_dlib_total)


                # 카운트 초기화
                COUNTER = 0
                seconds_dlib = 0
                minutes_dlib = 0
                hours_dlib = 0
                print("**EAR 초기화")

                # 숫자를 화면에 글자로 나타냄
                cv2.putText(frame, "Blinks: {}".format(TOTAL), (10, 30),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                cv2.putText(frame, "EAR: {:.2f}".format(ear), (300, 30),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                cv2.putText(frame, "tired: {}".format(TIRED), (150, 30),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

        label_layer = []
        cv2.imshow("YOLOv3", frame)

        if seconds_total > 59:
            seconds_total = 0
            minutes_total = minutes_total + 1

        if minutes_total > 59:
            minutes_total = 0
            hours_total = hours_total + 1

        seconds_total = seconds + seconds_dlib_total
        minutes_total = minutes + minutes_dlib_total
        hours_total = hours + hours_dlib_total

        string_time = str(hours_total).zfill(2) + ":" + str(minutes_total).zfill(2) + ":" + str(seconds_total).zfill(2)


        get_write()
        print("최종으로 딴 짓을 한 시간 : ", '%02d' % hours_total, ":", '%02d' % minutes_total, ":", '%02d' % seconds_total)
        print("-------------끝-------------")

        key = cv2.waitKey(1) & 0xFF

        if key == ord("q"):
            break