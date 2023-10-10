package com.ej.defaultcamera_gallary_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.ej.defaultcamera_gallary_test.databinding.FragmentBlankBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlin.math.log


class BlankFragment : Fragment() {
    lateinit var binding : FragmentBlankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_blank,container,false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.idcard)

        val degree = ImageUtils.getDrawableRotation(requireContext(), R.drawable.idcard)

        // Live detection and tracking
//        val options = ObjectDetectorOptions.Builder()
//            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//            .enableClassification()  // Optional
//            .build()

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()  // Optional
            .build()
        val objectDetector = ObjectDetection.getClient(options)

        val image = InputImage.fromBitmap(bitmap, 0)

        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                if(detectedObjects.isEmpty()) return@addOnSuccessListener
                var resultObject : DetectedObject? = null;
                for (detectedObject in detectedObjects) {

                    val cropWidth = detectedObject.boundingBox.right - detectedObject.boundingBox.left
                    val cropHeight = detectedObject.boundingBox.bottom - detectedObject.boundingBox.top

                    val rate = cropWidth/(cropHeight.toFloat())


                    val objectSize = detectedObject.labels.size
                    val objectBox = detectedObject.boundingBox
                    val objectTrackingId = detectedObject.trackingId
                    for (label in detectedObject.labels) {
                        val labelText = label.text
                        val labelIndex = label.index
                    }
                }
                resultObject = detectedObjects[0]
                val cropLeft = resultObject!!.boundingBox.left
                val cropTop = resultObject.boundingBox.top
                val cropWidth = resultObject.boundingBox.right - resultObject.boundingBox.left
                val cropHeight = resultObject.boundingBox.bottom - resultObject.boundingBox.top

                val imageHeight = image.height
                val imageWidth = image.width
                val croppedBitmap: Bitmap = Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropWidth, cropHeight)
                activity?.let {
                    it.runOnUiThread {
//                        binding.imageView.setImageBitmap(croppedBitmap)
                        binding.imageView.setImageBitmap(croppedBitmap)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d("EJTAG","f")
            }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BlankFragment()
    }
}