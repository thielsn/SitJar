/**
 * Pair.java
 *
 * 22-Apr-2010
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
