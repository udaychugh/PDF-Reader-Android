package com.udaychugh.pdfviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File

class pdfview : AppCompatActivity() {

    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        supportActionBar?.hide()

        PRDownloader.initialize(applicationContext)
        checkPDFAction(intent)

        progressBar = findViewById(R.id.progressbar)
    }

    fun getPdfNameFromAssets(): String{
        return "tnc.pdf"
    }

    private fun checkPDFAction(intent: Intent) {
        when (intent.getStringExtra("ViewType")){
            "assests" -> {
                showPdfFromAssests(FileUtils.getPdfNameFromAssets())
            }
            "storage" -> {
                selectPdfFromStorage()
            }
            "internet" -> {
                progressBar.visibility = View.VISIBLE
                val fileName = "myfile.pdf"
                downloadPdfFromInternet(
                        FileUtils.getPdfurl(),
                        FileUtils.getRootDirPath(this),
                        fileName
                )
            }

        }
    }

    private fun showPdfFromAssests(pdfname: String){
        pdfview.fromAsset(pdfname)
                .password(null)
                .defaultPage(0)
                .onPageError { page, _->
                    Toast.makeText(this@pdfview, "Error on this page : $page", Toast.LENGTH_SHORT).show()

                }.load()
    }

    companion object{
        private const val PDF_SELECTION_CODE = 99
    }

    private fun selectPdfFromStorage() {
        Toast.makeText(this, "Select PDF", Toast.LENGTH_SHORT).show()
        val browserStorage = Intent(Intent.ACTION_GET_CONTENT)
        browserStorage.type = "application/pdf"
        browserStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
                Intent.createChooser(browserStorage, "Select PDF"), PDF_SELECTION_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data!=null) {
            val selectedPdffromStorage = data.data
            showPdfFromAssests(selectedPdffromStorage)
        }
    }

    private fun showPdffromUri(uri : Uri?) {
        pdfview.fromUri(uri)
                .defaultPage(0)
                .spacing(10)
                .load()
    }

    private fun showPdfFromFile(file: File){
        pdfview.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageError { page, _->
                    Toast.makeText(this@pdfview, "Error on this page : $page", Toast.LENGTH_SHORT).show()

                }.load()
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
                url,
                dirPath,
                fileName
        ).build()
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Toast.makeText(this@pdfview, "Download Complete", Toast.LENGTH_SHORT).show()
                        val downloadedFile = File(dirPath, fileName)
                        progressBar.visibility = View.GONE
                        showPdfFromFile(downloadedFile)
                    }

                    override fun onError(error: Error?) {
                        Toast.makeText(this@pdfview, "Error downloading file : $error", Toast.LENGTH_SHORT).show()
                    }

                })
    }


}