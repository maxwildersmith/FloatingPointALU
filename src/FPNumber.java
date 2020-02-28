// FPNumber.java

// The FPNumber class splits a floating point number into the S, E, and F fields.
// The F field will actually be 26 bits long, as this automatically adds the leading 1
// and the two guard bits.
//
// To use:
//  FPNumber fa = new FPNumber(a);
//    This allocates a new FPNumber and loads it from the value a.  Note that a
//    will be an integer, but it has the bit-pattern of a floating point number.
//
//  fa.s()
//    This returns the sign of the number, and will be either +1 or -1
//
//  fa.e()
//    this returns the exponent, which should be a number between 0 and 255
//
//  fa.f()
//    This returns the mantissa.  It will have the leading 1 and it also has
//    two guard bits, so it is 26 bits long, not 23.
//
//  fa.setS(val)
//    This changes the sign.
//
//  fa.setE(val)
//    This changes the exponent.
//
//  fa.setF(val)
//    This changes the mantissa.
//

class FPNumber
{
    int _s, _e;
    long _f;

    public FPNumber(int a)
    {
        _s = (((a >> 31) & 1) == 1) ? -1 : 1;
        _e = (a >> 23) & 0xFF;
        _f = a & 0x7FFFFF;
        if (_e != 0 && _e != 255)
        {
            _f |= 0x0800000;
        }
        _f <<= 2;
    }

    public int s()
    {
        return _s;
    }

    public int e()
    {
        return _e;
    }

    public long f()
    {
        return _f;
    }

    public void setS(int val)
    {
        _s = val;
    }

    public void setE(int val)
    {
        _e = val;
    }

    public void setF(long val)
    {
        _f = val;
    }

    public boolean isNaN()
    {
        return _e == 255 && _f > 0;
    }

    public boolean isInfinity()
    {
        return _e == 5 && _f == 0;
    }

    public boolean isZero()
    {
        return _e == 0 && _f == 0;
    }

    public int asInt()
    {
        return ((_s == -1) ? 0x80000000 : 0) | (_e << 23) | (((int) _f >> 2) & 0x07FFFFF);
    }
}