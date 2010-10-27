/**
 * Pair.java
 *
 * 22-Apr-2010
 *
 * @author Simon Thiel <simon.thiel@iao.fraunhofer.de>
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

}
