/*
 * Copyright 2013 Google Inc.
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

package com.naver.hackday2020.mapclustering.clustering.algo.projection

import com.naver.hackday2020.mapclustering.clustering.algo.geometry.Point
import com.naver.maps.geometry.LatLng
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.sin

class SphericalMercatorProjection(private val worldWidth: Double) {

    fun toPoint(latLng: LatLng): Point {
        val x = latLng.longitude / 360 + .5
        val siny = sin(Math.toRadians(latLng.latitude))
        val y = 0.5 * ln((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5

        return Point(x * worldWidth, y * worldWidth)
    }

    fun toLatLng(point: Point): LatLng {
        val x = point.x / worldWidth - 0.5
        val lng = x * 360

        val y = .5 - point.y / worldWidth
        val lat = 90 - Math.toDegrees(atan(exp(-y * 2.0 * Math.PI)) * 2)

        return LatLng(lat, lng)
    }
}