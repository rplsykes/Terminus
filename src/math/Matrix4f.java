package math;

public class Matrix4f
{
    // Our 4x4 matrix.
    private float[][] matrix;

    public Matrix4f(float[][] matrix)
    {
        this.matrix = matrix;
        this.setIdentity();
    }

    public Matrix4f()
    {
        this.matrix = new float[4][4];
        this.setIdentity();
    }

    // Creates a projection matrix.
    public void projection(float fov, float near_plane, float far_plane, float screenWidth, float screenHeight)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] projMatrix = matrix.getRawMatrix();

        float aspectRatio = screenWidth / screenHeight;
        float y_scale = (float)(1.0f / Math.tan(Math.toRadians(fov / 2.0f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustrum_length = far_plane - near_plane;

        projMatrix[0][0] = x_scale;
        projMatrix[1][1] = y_scale;
        projMatrix[2][2] = -((far_plane + near_plane) / frustrum_length);
        projMatrix[2][3] = -1;
        projMatrix[3][2] = -((2 * near_plane * far_plane) / frustrum_length);
        projMatrix[3][3] = 0;

        this.multiplyAsFloats(this.matrix, projMatrix);
        this.transpose();
    }

    // Creates the view matrix.
    public void view(Vector3f position, Vector3f rotation)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] viewMatrix = matrix.getRawMatrix();




        this.multiplyAsFloats(this.matrix, viewMatrix);
    }

    // Creates a translation matrix;
    public void translate(Vector3f translationVector)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] translationMatrix = matrix.getRawMatrix();

        // Changes for the translation matrix.
        translationMatrix[0][3] = translationVector.x;
        translationMatrix[1][3] = translationVector.y;
        translationMatrix[2][3] = translationVector.z;
        translationMatrix[3][3] = translationVector.w;

        this.multiplyAsFloats(this.matrix, translationMatrix);
    }

    /*
     * Generates a scale matrix.
     */
    public void scale(Vector3f scaleVector)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] scaleMatrix = matrix.getRawMatrix();

        scaleMatrix[0][0] = scaleVector.x;
        scaleMatrix[1][1] = scaleVector.y;
        scaleMatrix[2][2] = scaleVector.z;
        scaleMatrix[3][3] = scaleVector.w;

        // Multiply this by the existing matrix.
        this.multiplyAsFloats(this.matrix, scaleMatrix);
    }

    // Rotation matrix.
    public void rotate(Vector3f rotation)
    {
        // Multiply the rotation x.
        this.multiplyAsFloats(this.matrix, this.buildRotationX(rotation.x));
        this.multiplyAsFloats(this.matrix, this.buildRotationY(rotation.y));
        this.multiplyAsFloats(this.matrix, this.buildRotationZ(rotation.z));
    }

    // Creates a translation matrix;
    public void setIdentity()
    {
        this.matrix[0][0] = 1; this.matrix[0][1] = 0; this.matrix[0][2] = 0; this.matrix[0][3] = 0;
        this.matrix[1][0] = 0; this.matrix[1][1] = 1; this.matrix[1][2] = 0; this.matrix[1][3] = 0;
        this.matrix[2][0] = 0; this.matrix[2][1] = 0; this.matrix[2][2] = 1; this.matrix[2][3] = 0;
        this.matrix[3][0] = 0; this.matrix[3][1] = 0; this.matrix[3][2] = 0; this.matrix[3][3] = 1;
    }

    public float[][] getRawMatrix()
    {
        return this.matrix;
    }

    // Converts the matrix data into a simple single-dimensional array.
    public float[] getAsSimpleArray()
    {
        float[] data = new float[16];

        int index = 0;

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j ) {
                data[index] = this.matrix[i][j];
                index++;
            }
        }

        return data;
    }

    // Reverse the matrix so its column major for opengl.
    public void transpose()
    {
        float[][] transpose = new float[4][4];

        for ( int i = 0; i < this.matrix[0].length; i++ ) {
            for ( int j = 0; j < this.matrix.length; j++ ) {
                transpose[j][i] = this.matrix[i][j];
            }
        }

       this.matrix = transpose;
    }

    @Override
    public String toString()
    {
        String matrixString = "";

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                matrixString += "["+this.matrix[i][j]+"]";

                if ( j == 3 ) {
                    matrixString += "\n";
                }
            }
        }

        return matrixString;
    }

    // Multiplies two float arrays.
    private void multiplyAsFloats(float[][] m, float[][] n)
    {
        float[][] newMatrix = new float[4][4];

        int aRows = m.length;
        int aCols = m[0].length;

        int bRows = n.length;
        int bCols = n[0].length;

        for ( int i = 0; i < aRows; i++) {
            for ( int j = 0; j < bCols; j++ ) {
                for ( int k = 0; k < aCols; k++ ) {
                    newMatrix[i][j] += m[i][k] * n[k][j];
                }
            }
        }

        this.matrix = newMatrix;
    }

    // Builds an X rotation matrix.
    private float[][] buildRotationX(float x)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] rotXMatrix = matrix.getRawMatrix();

        rotXMatrix[1][1] = (float)Math.cos(Math.toRadians(x));
        rotXMatrix[1][2] = -((float)Math.sin(Math.toRadians(x)));
        rotXMatrix[2][1] = (float)Math.sin(Math.toRadians(x));
        rotXMatrix[2][2] = (float)Math.cos(Math.toRadians(x));

        return rotXMatrix;
    }

    private float[][] buildRotationY(float y)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] rotYMatrix = matrix.getRawMatrix();

        rotYMatrix[0][0] = (float)Math.cos(Math.toRadians(y));
        rotYMatrix[0][2] = (float)Math.sin(Math.toRadians(y));
        rotYMatrix[2][0] = -((float)Math.sin(Math.toRadians(y)));
        rotYMatrix[2][2] = (float)Math.cos(Math.toRadians(y));

        return rotYMatrix;
    }

    private float[][] buildRotationZ(float z)
    {
        Matrix4f matrix = new Matrix4f();
        float[][] rotZMatrix = matrix.getRawMatrix();

        rotZMatrix[0][0] = (float)Math.cos(Math.toRadians(z));
        rotZMatrix[0][1] = -((float)Math.sin(Math.toRadians(z)));
        rotZMatrix[1][0] = (float)Math.sin(Math.toRadians(z));
        rotZMatrix[1][1] = (float)Math.cos(Math.toRadians(z));

        return rotZMatrix;
    }
}
