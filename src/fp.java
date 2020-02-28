public class fp {
    // Be sure to put your name on this next line...
    public String myName()
    {
        return "Maximum Wilder-Smith";
    }

    public int add(int a, int b)
    {
        FPNumber fa = new FPNumber(a);
        FPNumber fb = new FPNumber(b);
        FPNumber result = new FPNumber(0);

        //Exception checking
        if(fa.isNaN()||fb.isNaN()) {
            result.setE(255);
            result.setF(1);
        }
        if(fa.isZero())
            result = fb;

        if(fb.isZero())
            result = fa;

        if(fa.isInfinity()&&fb.isInfinity()){
            if(fa.s()==fb.s())
                result = fa;
            else{
                result.setF(1);
                result.setE(255);
            }
        }

        else if(fa.isInfinity()||fb.isInfinity()){
            result = fa.isInfinity() ? fa : fb;
        }

        if((fa.e()==fb.e()&&fa.f()>fb.f())||fa.e()>fb.e()){  //fa > fb
            if(fa.e()-fb.e()>24)
                result = null;
        }

        return result.asInt();
    }

    public int mul(int a, int b)
    {
        FPNumber fa = new FPNumber(a);
        FPNumber fb = new FPNumber(b);
        FPNumber result = new FPNumber(0);

        // Put your code in here!

        return result.asInt();
    }

    // Here is some test code that one student had written...
    public static void main(String[] args)
    {
        int v24_25	= 0x41C20000; // 24.25
        int v_1875	= 0xBE400000; // -0.1875
        int v5		= 0xC0A00000; // -5.0

        fp m = new fp();

        System.out.println(Float.intBitsToFloat(m.add(v24_25, v_1875)) + " should be 24.0625");
        System.out.println(Float.intBitsToFloat(m.add(v24_25, v5)) + " should be 19.25");
        System.out.println(Float.intBitsToFloat(m.add(v_1875, v5)) + " should be -5.1875");

        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v_1875)) + " should be -4.546875");
        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v5)) + " should be -121.25");
        System.out.println(Float.intBitsToFloat(m.mul(v_1875, v5)) + " should be 0.9375");
    }

}
