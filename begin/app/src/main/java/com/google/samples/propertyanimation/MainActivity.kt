/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
            }
        })
    }

    private fun rotater() = ObjectAnimator
        .ofFloat(star, View.ROTATION, -360f, 0f)
        .setDuration(1_000)
        .apply {
            disableViewDuringAnimation(rotateButton)
        }
        .start()

    private fun translater() = ObjectAnimator
        .ofFloat(star, View.TRANSLATION_X, 200f)
        .apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(translateButton)
        }
        .start()

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        ObjectAnimator
            .ofPropertyValuesHolder(star, scaleX, scaleY)
            .apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableViewDuringAnimation(scaleButton)
            }
            .start()
    }

    private fun fader() = ObjectAnimator
        .ofFloat(star, View.ALPHA, 0f)
        .apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(fadeButton)
        }
        .start()

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer() = ObjectAnimator
        .ofArgb(
            star.parent,
            "backgroundColor",
            Color.BLACK,
            Color.RED
        )
        .setDuration(500)
        .apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(colorizeButton)
        }
        .start()

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(this)
            .apply {
                setImageResource(R.drawable.ic_star)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            }

        container.addView(newStar)

        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2

        val mover = ObjectAnimator
            .ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
            .apply {
                interpolator = AccelerateInterpolator(1f)
            }

        val rotator = ObjectAnimator
            .ofFloat(newStar, View.ROTATION, (Math.random() * 1080).toFloat())
            .apply {
                interpolator = LinearInterpolator()
            }

        AnimatorSet()
            .apply {
                playTogether(mover, rotator)
            }
            .setDuration((Math.random() * 1500 + 500).toLong())
            .start()
    }

}
