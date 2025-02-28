package com.example.photogallery.classifier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.example.photogallery.ml.MobilenetV3TfliteLarge075224ClassificationV1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.IOException

class ImageClassifier(private val context: Context) {

    private val model by lazy {
        MobilenetV3TfliteLarge075224ClassificationV1.newInstance(context)
    }

    private fun loadLabels(): List<String> {
        return context.assets.open("labels_zh.txt").bufferedReader().use { it.readLines() }
    }

    private fun getResizedBitmapFromUri(
        imageUri: Uri,
        width: Int = 224,
        height: Int = 224
    ): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            val originalBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.setTargetColorSpace(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB))
            }
            val argbBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
            Bitmap.createScaledBitmap(argbBitmap, width, height, true)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun classifyImage(imageUri: Uri): String {
        val bitmap = getResizedBitmapFromUri(imageUri) ?: return "未知"
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 255.0f))
            .build()

        val processedTensorImage = imageProcessor.process(tensorImage)

        val outputs = model.process(processedTensorImage.tensorBuffer)
        val probabilities = outputs.outputFeature0AsTensorBuffer.floatArray

        val labels = loadLabels()
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        return labels.getOrElse(maxIndex) { "未命名" }
    }

    fun close() {
        model.close()
    }
}