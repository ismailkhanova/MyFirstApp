import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.WeatherModel
import com.example.myfirstapp.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter = WeatherAdapter()
        rcView.adapter = adapter
        val list = listOf(
            WeatherModel(
                "","21:00",
                "Cloudy","",
                "-16ºC", "", "","",
                "", ""),
            WeatherModel(
                "","22:00",
                "Cloudy","",
                "-15ºC", "", "","", "", ""),
            WeatherModel(
                "","23:00",
                "Cloudy","",
                "-16ºC", "", "","",
                "", ""),
            WeatherModel(
                "","00:00",
                "Cloudy","",
                "-16ºC", "", "","",
                "", ""),
            WeatherModel(
                "","1:00",
                "Cloudy","",
                "-17ºC", "", "","",
                "", "")
        )
        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}