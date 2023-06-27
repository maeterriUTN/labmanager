package com.utn.labmanager.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.utn.labmanager.MainActivity
import com.utn.labmanager.R
import com.utn.labmanager.ScanActivity
import com.utn.labmanager.entities.QrCodeAnalyzer
//import com.utn.labmanager.textureView

class ScanFragment : Fragment() {

    private lateinit var v : View
    private lateinit var button_scan : Button
    private lateinit var textureView: PreviewView

    companion object {
        fun newInstance() = ScanFragment()
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_scan, container, false)
        button_scan= v.findViewById(R.id.button_scan)
        textureView = v.findViewById(R.id.texture_view)

        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                ScanActivity.REQUEST_CAMERA_PERMISSION
            )
        }

        return v
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onStart() {
        super.onStart()


        button_scan.setOnClickListener {

            Snackbar.make(v,"Botón Apretado", Snackbar.LENGTH_LONG).show()




        }
    }

    private fun startCamera() {
        var cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        val cameraSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val previewConfig = Preview.Builder()
            // We want to show input from back camera of the device
            .setTargetResolution(Size(400, 400))
            .build()

        previewConfig.setSurfaceProvider(textureView.createSurfaceProvider())


        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetResolution(Size(400, 400))
            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits requested capture mode
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .build()
        val executor = ContextCompat.getMainExecutor(requireContext())
        val imageAnalyzer = ImageAnalysis.Builder().build().also {
            it.setAnalyzer(executor, QrCodeAnalyzer { qrCodes ->
                qrCodes?.forEach {
                    Log.d("MainActivity", "QR Code detected: ${it.rawValue}.")
                    Toast.makeText(context, "Se detectó un Qr ${it.rawValue}", Toast.LENGTH_SHORT).show()

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Reactivo Detectado")
                    builder.setMessage("Confirme agregar ${it.rawValue}")


                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(context,
                            android.R.string.yes, Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(context,
                            android.R.string.no, Toast.LENGTH_SHORT).show()
                    }

                    builder.show()



                }
            })


        }


        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                previewConfig,
                imageCapture,
                imageAnalyzer
            )

//            Handle flash
            camera.cameraControl.enableTorch(false)
        }, executor)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(requireActivity().baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ScanActivity.REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                //finish()
            }
        }
    }


}
