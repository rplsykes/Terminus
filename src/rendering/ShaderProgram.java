package rendering;

import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram
{
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile)
    {
        this.vertexShaderID = this.loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = this.loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        this.programID = GL20.glCreateProgram();

        GL20.glAttachShader(this.programID, vertexShaderID);
        GL20.glAttachShader(this.programID, fragmentShaderID);
        this.bindAttributes();
        GL20.glLinkProgram(this.programID);
        GL20.glValidateProgram(this.programID);
        this.getAllUniformLocations();
    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector)
    {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value)
    {
        float toLoad = 0;

        if ( value ) {
            toLoad = 1;
        }

        GL20.glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix)
    {
        matrix.transpose();
        this.matrixBuffer.put(matrix.getAsSimpleArray());
        this.matrixBuffer.flip();
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected int getUniformLocation(String uniformName)
    {
        return GL20.glGetUniformLocation(this.programID, uniformName);
    }

    protected void bindAttribute(int attribute, String varName)
    {
        GL20.glBindAttribLocation(this.programID, attribute, varName);
    }

    public void start()
    {
        GL20.glUseProgram(this.programID);
    }

    public void stop()
    {
        GL20.glUseProgram(0);
    }

    public void clean()
    {
        this.stop();

        GL20.glDetachShader(this.programID, this.vertexShaderID);
        GL20.glDetachShader(this.programID, this.fragmentShaderID);
        GL20.glDeleteShader(this.vertexShaderID);
        GL20.glDeleteShader(this.fragmentShaderID);
        GL20.glDeleteProgram(this.programID);
    }

    private static int loadShader(String file, int type)
    {
        StringBuilder shaderSource = new StringBuilder();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }
        catch(IOException e){
            System.err.println("Could not read file!");
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Could not compile shader!");
            System.exit(-1);
        }

        return shaderID;
    }
}
