package com.example.getyourdrink.shaker

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.getyourdrink.R
import com.example.getyourdrink.databinding.FragmentShakerBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShakerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShakerFragment : Fragment(), SensorEventListener {

    private var animActive = true

    lateinit var binding: FragmentShakerBinding

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var sensors: Sensor? = null
    private lateinit var sensorManager: SensorManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shaker, container, false
        )
        var ingredientsName: Array<String> = requireArguments()["ingredientsNames"] as Array<String>

        if (ingredientsName == null)
            ingredientsName = arrayOf<String>("All")


        sensorManager = getSystemService(
            requireActivity().applicationContext,
            SensorManager::class.java
        ) as SensorManager

        binding.textView.text = ""
        for (i in 1..Math.min(3, ingredientsName.size)) {
            if (i % 2 == 0)
                binding.textView.text =
                    binding.textView.text.toString() + "  ".repeat(i) + ingredientsName[i - 1] + "\n"
            else
                binding.textView.text =
                    binding.textView.text.toString() + " ".repeat(i) + ingredientsName[i - 1]

        }
        if (ingredientsName.size >= 4)
            binding.textView.text = binding.textView.text.toString() + ingredientsName[3]


        binding.shakerTogether.setOnClickListener {
            // If the user stopped phone from moving too quick
            if (stable) {
                stable = false
                sensorManager.unregisterListener(this)
                animateEnd()
                requireView().postDelayed({
                    this.findNavController().navigate(
                        ShakerFragmentDirections.actionShakerFragmentToDrinksFragment(
                            ingredientsName
                        )
                    )
                }, 5000)
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Animation

        // 3 parts of animation
        val animSet1 = AnimatorSet()
        val animSet2 = AnimatorSet()
        val animSet3 = AnimatorSet()

        val delay: Long = 1000
        // #1 Opening of shaker
        requireView().postDelayed({
            val animTime: Long = 750
            animSet1.playTogether(
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationX", -200f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationY", -200f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "rotation", -90f)
                    .setDuration(animTime)
            )
            animSet1.start()
        }, delay)

        // #2 Getting ingredients
        requireView().postDelayed({
            val animTime: Long = 2000
            animSet2.playTogether(
                ObjectAnimator.ofFloat(binding.textView, "translationY", 900f)
                    .setDuration(animTime)
            )
            animSet2.start()
        }, delay * 2)

        // #3 Closing the shaker
        requireView().postDelayed({
            val animTime: Long = 750
            animSet3.playTogether(
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationX", 0f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationY", 0f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "rotation", 0f).setDuration(animTime)
            )
            animSet3.start()
        }, delay * 3)

        // #4 Hiding ingredients, an enabling sensor animation
        requireView().postDelayed({
            val animTime: Long = 250
            binding.shakerClosure.visibility = View.INVISIBLE
            binding.textView.visibility = View.INVISIBLE
            binding.shakerOpen.visibility = View.INVISIBLE
            binding.shakerTogether.visibility = View.VISIBLE


        }, delay * 6 + 500)


        requireView().postDelayed({
            animActive = false
        }, delay * 7)

    }

    override fun onResume() {
        super.onResume()
        //Register the sensor on resume of the activity
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometr ->
            sensorManager.registerListener(
                this, accelerometr,
                3, SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this, magneticField,
                3, SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onStop() {
        // Unregister the listener
        sensorManager.unregisterListener(this)
        super.onStop()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }


    override fun onSensorChanged(event: SensorEvent?) {
        // 1
        if (event == null) {
            return
        }
        // 2
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 3
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()

    }

    var lastAngle: Double = -999.0
    var countR = 0
    var angleValue = 0f
    var actualValue = 0f
    val animTime = 150L
    var firstTime = true
    var changePosValue = 0.5f
    var stable = false
    val countResetVal = 15
    private fun getDirection(angle: Double): String {
        var direction = ""

        if (lastAngle == -999.0)
            lastAngle = angle


        if (!animActive) {
            val animSet = AnimatorSet()
            changePosValue += 0.02f

            if (Math.abs(lastAngle - angle) > 45 && !firstTime) {
                stable = false
                countR = 0
            }


            if (countR >= countResetVal) {
                if (firstTime) {
                    firstTime = false
                    actualValue = angleValue
                    return ""
                }
                changePosValue = 0.15f
                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    ).setDuration(animTime * 5),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        0f
                    ).setDuration(animTime * 5),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        0f
                    )
                        .setDuration(animTime * 5),
                )
                animSet.start()
                stable = true

                requireView().postDelayed({
                    animActive = false
                    actualValue = angleValue
                }, animTime * 5)
            } else if (angle <= 45) {

                angleValue = 0f
                lastAngle = 45.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }
                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )

                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 90 && angle > 45) {
                angleValue = 45f
                lastAngle = 90.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }
                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 135 && angle > 90) {
                angleValue = 90f
                lastAngle = 135.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 180 && angle > 135) {
                angleValue = 115f
                lastAngle = 180.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 225 && angle > 180) {

                angleValue = 0f
                lastAngle = 225.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 270 && angle > 225) {
                angleValue = -115f
                lastAngle = 270.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 315 && angle > 270) {
                angleValue = -90f
                lastAngle = 315.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            } else if (angle <= 360 && angle > 315) {
                angleValue = -45f
                lastAngle = 360.0
                countR++
                if (firstTime) {
                    countR = countResetVal
                    return ""
                }

                animActive = true
                animSet.playTogether(
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "rotation",
                        angleValue - actualValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationX",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime),
                    ObjectAnimator.ofFloat(
                        binding.shakerTogether,
                        "translationY",
                        (angleValue - actualValue) * changePosValue
                    )
                        .setDuration(animTime)
                )
                animSet.start()
                requireView().postDelayed({ animActive = false }, animTime)
            }


        }
        return direction
    }

    fun updateOrientationAngles() {
        // 1
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        // 2
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // 3
        val degrees = (Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0

        getDirection(degrees)
    }


    fun animateEnd() {
        val delay = 250L
        val animSetEnd1 = AnimatorSet()
        val animSetEnd2 = AnimatorSet()
        val animSetEnd3 = AnimatorSet()
        val animSetEnd4 = AnimatorSet()

        // #1 Little shake at the start 2s
        requireView().postDelayed({
            val animTime: Long = delay
            animSetEnd1.playTogether(
                ObjectAnimator.ofFloat(binding.shakerTogether, "translationX", 100f)
                    .setDuration(animTime),
            )
            animSetEnd1.start()
        }, delay)

        requireView().postDelayed({
            val animTime: Long = delay
            animSetEnd1.playTogether(
                ObjectAnimator.ofFloat(binding.shakerTogether, "translationX", -100f)
                    .setDuration(animTime),
            )
            animSetEnd1.start()
        }, delay * 2)
        requireView().postDelayed({
            val animTime: Long = delay
            animSetEnd1.playTogether(
                ObjectAnimator.ofFloat(binding.shakerTogether, "translationX", 100f)
                    .setDuration(animTime),
            )
            animSetEnd1.start()
        }, delay * 3)
        requireView().postDelayed({
            val animTime: Long = delay
            animSetEnd1.playTogether(
                ObjectAnimator.ofFloat(binding.shakerTogether, "translationX", -100f)
                    .setDuration(animTime),
            )
            animSetEnd1.start()
        }, delay * 4)

        // #2 Getting ingredients
        requireView().postDelayed({
            val animTime: Long = delay
            binding.shakerClosure.visibility = View.VISIBLE
            binding.shakerOpen.visibility = View.VISIBLE
            binding.shakerTogether.visibility = View.INVISIBLE
        }, delay * 6)

        // #3 Throwing the closure away
        requireView().postDelayed({
            val animTime: Long = delay * 5
            animSetEnd2.playTogether(
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationX", -800f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "translationY", -800f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerClosure, "rotation", -180f)
                    .setDuration(animTime)
            )
            animSetEnd2.start()
        }, delay * 8)


        // #4 Pouring the drink list!
        requireView().postDelayed({
            val animTime: Long = delay * 6
            animSetEnd4.playTogether(
                ObjectAnimator.ofFloat(binding.shakerOpen, "translationX", 90f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerOpen, "translationY", -150f)
                    .setDuration(animTime),
                ObjectAnimator.ofFloat(binding.shakerOpen, "rotation", 90f)
                    .setDuration(animTime)
            )
            animSetEnd4.start()
        }, delay * 17)

    }
}
