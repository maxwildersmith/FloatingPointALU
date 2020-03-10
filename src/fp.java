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

        long tmp;

        if((fa.e()==fb.e()&&fa.f()>fb.f())||fa.e()>fb.e()){  //fa > fb
            if(fa.s()==fb.s()) {
                tmp = fa.f() + (fb.f() >> 3);
            } else {
                tmp = fa.f() - (fb.f() >> 3);
                if(tmp==0){
                    result = new FPNumber(0);
                }
            }
            if(((tmp >> 26) & 1) == 1){
                result.setF(1);
                result.setE(255);
                result.setS(fa.s());
            } else {
                int tmpE = fa.e();
                while(((tmp >> 25) & 1) == 0){
                    tmp = tmp << 1;
                    tmpE--;
                    if(tmpE == 0){
                        tmp=tmp<<1;
                        result.setF(tmp);
                        result.setE(0);
                        break;
                    }
                }
                result.setF(tmp);
                result.setE(tmpE);
                result.setS(fa.s());
            }

        } else {
            if(fa.s()==fb.s()) {
                tmp = fb.f() + (fa.f() >> 3);
            } else {
                tmp = fb.f() - (fa.f() >> 3);
                if(tmp==0){
                    result = new FPNumber(0);
                }
            }
            if(((tmp >> 26) & 1) == 1){
                result.setF(1);
                result.setE(255);
                result.setS(fb.s());
            } else {
                int tmpE = fb.e();
                while(((tmp >> 25) & 1) == 0){
                    tmp = tmp << 1;
                    tmpE--;
                    if(tmpE == 0){
                        tmp=tmp<<1;
                        result.setF(tmp);
                        result.setE(0);
                        break;
                    }
                }
                result.setF(tmp);
                result.setE(tmpE);
                result.setS(fb.s());
            }
        }

        return result.asInt();
    }

    public int mul(int a, int b)
    {
        FPNumber fa = new FPNumber(a);
        FPNumber fb = new FPNumber(b);
        FPNumber result = new FPNumber(0);

        // Put your code in here!
        if(fa.isNaN())
            return fa.asInt();
        if(fb.isNaN())
            return fb.asInt();
        if((fa.isZero()&&fb.isInfinity())||(fb.isZero()&&fa.isInfinity())){
            result.setF(1);
            result.setE(255);
            return result.asInt();
        }
        if(fa.isZero()||fb.isZero()){
            result.setF(0);
            result.setE(0);
            return result.asInt();
        }
        result.setS((fa.s()^fb.s())<0?-1:0);
        if(fa.isInfinity()||fb.isInfinity()){
            result.setF(0);
            result.setE(255);
            return result.asInt();
        }
        int tmpE = fa.e()+fb.e();
        tmpE = tmpE - 127;
        if(tmpE>254){
            result.setF(0);
            result.setE(255);
            return result.asInt();
        } else if(tmpE<0){
            result.setF(0);
            result.setE(0);
            return result.asInt();
        }
        long tmpF = fa.f() * fb.f();
        tmpF = tmpF >> 25;
        
        result.setE(tmpE);
        result.setF(tmpF);
        print(result);
        return result.asInt();
    }

    public static void print(FPNumber n){
        System.out.print("Mantissa: ");
        bin(n.f());
        System.out.print("Exponent: "+n.e()+"  -  ");
        bin(n.e());
        System.out.println("Sign: "+n.s());
    }

    public static void bin(int n){
        System.out.println(Integer.toBinaryString(n));
    }
    public static void bin(long n){
        System.out.println(Long.toBinaryString(n));
    }

    // Here is some test code that one student had written...
    public static void main(String[] args)
    {
        int v24_25	= 0x41C20000; // 24.25
        int v_1875	= 0xBE400000; // -0.1875
        int v5		= 0xC0A00000; // -5.0
        int v13_75  = 0x415C0000;
        int v47_625 = 0x423E8000;

        fp m = new fp();
        System.out.println(Float.intBitsToFloat(m.mul(v13_75, v47_625)) + " should be  654.84375");


        System.out.println(Float.intBitsToFloat(m.add(v24_25, v_1875)) + " should be 24.0625");
        System.out.println(Float.intBitsToFloat(m.add(v24_25, v5)) + " should be 19.25");
        System.out.println(Float.intBitsToFloat(m.add(v_1875, v5)) + " should be -5.1875");



        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v_1875)) + " should be -4.546875");
        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v5)) + " should be -121.25");
        System.out.println(Float.intBitsToFloat(m.mul(v_1875, v5)) + " should be 0.9375");
    }

}
