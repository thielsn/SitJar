/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/**
 * Pair.java
 *
 * 22-Apr-2010
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */



package sit.sstl;


/**
 * class Pair
 *
 */
public class Pair<A,B> {

    private A a;
    private B b;

    public Pair(){
        a=null;
        b=null;
    }

    public Pair(A a, B b){
        this.a = a;
        this.b = b;
    }

    public A getA(){
        return a;
    }

    public B getB(){
        return b;
    }

    public void setA(A a){
        this.a = a;
    }

    public void setB(B b){
        this.b = b;
    }

    @Override
    public int hashCode() {
        return (a.hashCode()+(17*b.hashCode()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<A, B> other = (Pair<A, B>) obj;
        if (this.a != other.a && (this.a == null || !this.a.equals(other.a))) {
            return false;
        }
        if (this.b != other.b && (this.b == null || !this.b.equals(other.b))) {
            return false;
        }
        return true;
    }




}
