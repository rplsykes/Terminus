package rendering.shaders;


import math.Matrix4f;
import rendering.ShaderProgram;

public class StaticShader extends ShaderProgram
{
    private static final String VERTEX_FILE = "src/shaders/vertexShader.glsl";
    private static final String FRAG_FILE = "src/shaders/fragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader()
    {
        super(VERTEX_FILE, FRAG_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations()
    {
        this.location_transformationMatrix =  super.getUniformLocation("transformationMatrix");
        this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        this.location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(this.location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix)
    {
        super.loadMatrix(this.location_viewMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f proj)
    {
        super.loadMatrix(this.location_projectionMatrix, proj);
    }
}
