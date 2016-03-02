package math;

public class Vector3f
{
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public float w = 1.0f;

    public Vector3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(float[] floatData)
    {
        this.x = floatData[0];
        this.y = floatData[1];
        this.z = floatData[2];
    }

    public float[] toFloatArray()
    {
        float[] floats = new float[4];
        floats[0] = this.x;
        floats[1] = this.y;
        floats[2] = this.z;
        floats[3] = this.w;

        return floats;
    }

    @Override
    public String toString()
    {
        return "["+x+","+y+","+z+"]";
    }
}
