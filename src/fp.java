public class fp {
    // Be sure to put your name on this next line...
    public String myName() {
        return "Maximum Wilder-Smith";
    }

    public int add(int a, int b) {
        FPNumber fa = new FPNumber(a);
        FPNumber fb = new FPNumber(b);
        FPNumber result = new FPNumber(0);

        //Exception checking if either is NAN
        if (fa.isNaN() || fb.isNaN()) {
            result.setE(255);
            result.setF(255);
            return result.asInt();
        }

        // Check if zeros
        if (fa.isZero())
            return fb.asInt();
        if (fb.isZero())
            return fa.asInt();

        // Both are infinity check
        if (fa.isInfinity() && fb.isInfinity()) {
            if (fa.s() == fb.s())
                return fa.asInt();
            else {
                result.setE(255);
                result.setF(255);
                return result.asInt();
            }
        }

        // Check if either are infinity
        if (fa.isInfinity() || fb.isInfinity()) {
            return (fa.isInfinity() ? fa : fb).asInt();
        }

        // Sort numbers
        FPNumber larger, smaller;
        if (fa.e() > fb.e() || ((fa.e() == fb.e()) && fa.f() > fb.f())) {
            larger = fa;
            smaller = fb;
        } else {
            larger = fb;
            smaller = fa;
        }

        int eDif = larger.e() - smaller.e();

        // Smaller number is effectively zero
        if (eDif > 24)
            return larger.asInt();
        int tmpE = larger.e();
        smaller.setF(smaller.f() >> eDif);

        // Check signs and add or subtract
        long tmpF;
        if (larger.s() == smaller.s())
            tmpF = larger.f() + smaller.f();
        else {
            tmpF = larger.f() - smaller.f();
        }

        // Return zero if mantissa is zero
        if (tmpF == 0) {
            result.setE(0);
            result.setF(0);
            return result.asInt();
        }

        if (((tmpF >> 26) & 1) == 1) {
            tmpF = tmpF >> 1;
            tmpE++;
        }
        if (tmpE >= 255) {
            result.setE(255);
            result.setF(0);
            result.setS(larger.s());
            return result.asInt();
        }

        // Normalization
        while (((tmpF >> 25) & 1) == 0) {
            tmpF = tmpF << 1;
            tmpE--;
        }

        // For rounding, roughly
        tmpF += 2;

        if (tmpE <= 0) {
            tmpF = tmpF >> 1;
            tmpE = 0;
        }

        // Normalization if no longer normalized
        while (((tmpF >> 25) & 1) == 0) {
            tmpF = tmpF << 1;
            tmpE--;
        }
        if (tmpE <= 0) {
            tmpF = tmpF >> 1;
            tmpE = 0;
        }

        // Set result and return
        result.setS(larger.s());
        result.setE(tmpE);
        result.setF(tmpF);

        return result.asInt();
    }

    public int mul(int a, int b) {
        FPNumber fa = new FPNumber(a);
        FPNumber fb = new FPNumber(b);
        FPNumber result = new FPNumber(0);

        // Check if either are NAN
        if (fa.isNaN())
            return fa.asInt();
        if (fb.isNaN())
            return fb.asInt();

        // Check if one is infinity and other is zero
        if ((fa.isZero() && fb.isInfinity()) || (fb.isZero() && fa.isInfinity())) {
            result.setF(255);
            result.setE(255);
            return result.asInt();
        }

        // Check if one is zero
        if (fa.isZero() || fb.isZero()) {
            result.setF(0);
            result.setE(0);
            return result.asInt();
        }

        // Set sign as XOR of the signs
        result.setS((fa.s() ^ fb.s()) < 0 ? -1 : 0);

        // Check if one of them is infinity
        if (fa.isInfinity() || fb.isInfinity()) {
            result.setF(0);
            result.setE(255);
            return result.asInt();
        }

        // Set exponent and remove the bias
        int tmpE = fa.e() - 127 + fb.e();

        // Check for over/underflow
        if (tmpE > 254) {
            result.setF(0);
            result.setE(255);
            return result.asInt();
        } else if (tmpE < 0) {
            result.setF(0);
            result.setE(0);
            return result.asInt();
        }

        //Compute mantissa
        long tmpF = fa.f() * fb.f();

        // Shift bits
        tmpF = tmpF >> 26;
        tmpE++;

        // Normalize
        while (((tmpF >> 25) & 1) == 0) {
            tmpF = tmpF << 1;
            tmpE--;
        }

        //Handle rounding
        tmpF += 2;


        result.setE(tmpE);
        result.setF(tmpF);
        return result.asInt();
    }

    // Here is some test code that one student had written...
    public static void main(String[] args) {
        int v24_25 = 0x41C20000; // 24.25
        int v_1875 = 0xBE400000; // -0.1875
        int v5 = 0xC0A00000; // -5.0

        fp m = new fp();

        System.out.println(Float.intBitsToFloat(m.add(v24_25, v_1875)) + " should be 24.0625");
        System.out.println(Float.intBitsToFloat(m.add(v24_25, v5)) + " should be 19.25");
        System.out.println(Float.intBitsToFloat(m.add(v_1875, v5)) + " should be -5.1875");

        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v_1875)) + " should be -4.546875");
        System.out.println(Float.intBitsToFloat(m.mul(v24_25, v5)) + " should be -121.25");
        System.out.println(Float.intBitsToFloat(m.mul(v_1875, v5)) + " should be 0.9375");
    }

}
