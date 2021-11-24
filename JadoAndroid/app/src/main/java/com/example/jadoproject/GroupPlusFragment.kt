import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.FriendFragment
import com.example.jadoproject.HomeFragment
import com.example.jadoproject.JoinActivity
import com.example.jadoproject.R
import com.example.jadoproject.data.GName
import com.example.jadoproject.data.Group
import com.example.jadoproject.data.Members
import com.example.jadoproject.databinding.ActivityJoinBinding
import com.example.jadoproject.databinding.FragmentGroupPlusBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class GroupPlusFragment: AppCompatActivity() {
    private lateinit var binding: FragmentGroupPlusBinding
    private val friend= FriendFragment()
    private val gson by lazy { Gson() }

    // firebase 연동
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_group_plus)


        binding.plus.setOnClickListener {
            val Group : Group = Group(GName(
                arrayListOf() ,binding.gGoalInput.text.toString(), binding.gNameInput.text.toString()))


            onBackPressed()
        }

        binding.gPlusBtn.setOnClickListener {

            // 가입 창으로 이동
            val intent = Intent(this, FriendFragment::class.java)
            startActivity(intent)
        }
    }


}
