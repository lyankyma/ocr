package com.example.ocr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var recognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // Launch file chooser
        val pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { handleFile(it) }
        }

        // Trigger file chooser
        findViewById<Button>(R.id.select_file_button).setOnClickListener {
            pickFile.launch("application/pdf")
        }
    }

    private fun handleFile(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val fileType = contentResolver.getType(uri)

        if (fileType == "application/pdf") {
            renderPdf(inputStream!!)
        } else {
            performOCR(InputImage.fromFilePath(this, uri))
        }
    }

    private fun renderPdf(inputStream: InputStream) {
        val file = File.createTempFile("temp_pdf", ".pdf", cacheDir)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val pdfiumCore = PdfiumCore(this)
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfDocument = pdfiumCore.newDocument(fileDescriptor)
        pdfiumCore.openPage(pdfDocument, 0)

        val width = pdfiumCore.getPageWidthPoint(pdfDocument, 0)
        val height = pdfiumCore.getPageHeightPoint(pdfDocument, 0)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        pdfiumCore.renderPageBitmap(pdfDocument, bitmap, 0, 0, 0, width, height)

        pdfiumCore.closeDocument(pdfDocument)
        pdfiumCore.closeDocument(pdfDocument)
        fileDescriptor.close()
        inputStream.close()
        outputStream.close()

        // Display PDF Bitmap
        findViewById<ImageView>(R.id.image_view).setImageBitmap(bitmap)
        performOCR(InputImage.fromBitmap(bitmap, 0))
    }

    private fun performOCR(inputImage: InputImage) {
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                // Display recognized text
                findViewById<TextView>(R.id.ocr_result_text).text = visionText.text
            }
            .addOnFailureListener { e ->
                findViewById<TextView>(R.id.ocr_result_text).text = "OCR failed: ${e.message}"
            }
    }
}
