/*******************************************************************************
 * Copyright (c) 2011, Daniel Murphy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DANIEL MURPHY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/ 
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */
package org.jbox2d.common;

import java.io.Serializable;

// updated to rev 100

/**
 * @author Daniel Murphy
 */
public class Vec3 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public float x, y, z;
	
	public Vec3() {
		x = y = z = 0f;
	}
	
	public Vec3(float argX, float argY, float argZ) {
		x = argX;
		y = argY;
		z = argZ;
	}
	
	public Vec3(Vec3 argCopy) {
		x = argCopy.x;
		y = argCopy.y;
		z = argCopy.z;
	}
	
	public Vec3 set(Vec3 argVec) {
		x = argVec.x;
		y = argVec.y;
		z = argVec.z;
		return this;
	}
	
	public Vec3 set(float argX, float argY, float argZ) {
		x = argX;
		y = argY;
		z = argZ;
		return this;
	}
	
	public Vec3 addLocal(Vec3 argVec) {
		x += argVec.x;
		y += argVec.y;
		z += argVec.z;
		return this;
	}
	
	public Vec3 add(Vec3 argVec) {
		return new Vec3(x + argVec.x, y + argVec.y, z + argVec.z);
	}
	
	public Vec3 subLocal(Vec3 argVec) {
		x -= argVec.x;
		y -= argVec.y;
		z -= argVec.z;
		return this;
	}
	
	public Vec3 sub(Vec3 argVec) {
		return new Vec3(x - argVec.x, y - argVec.y, z - argVec.z);
	}
	
	public Vec3 mulLocal(float argScalar) {
		x *= argScalar;
		y *= argScalar;
		z *= argScalar;
		return this;
	}
	
	public Vec3 mul(float argScalar) {
		return new Vec3(x * argScalar, y * argScalar, z * argScalar);
	}
	
	public Vec3 negate() {
		return new Vec3(-x, -y, -z);
	}
	
	public Vec3 negateLocal() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public void setZero() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vec3 clone() {
		return new Vec3(this);
	}
	
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vec3 other = (Vec3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	
	public final static float dot(Vec3 a, Vec3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public final static Vec3 cross(Vec3 a, Vec3 b) {
		return new Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
	}
	
	public final static void crossToOut(Vec3 a, Vec3 b, Vec3 out) {
		final float tempy = a.z * b.x - a.x * b.z;
		final float tempz = a.x * b.y - a.y * b.x;
		out.x = a.y * b.z - a.z * b.y;
		out.y = tempy;
		out.z = tempz;
	}
}
