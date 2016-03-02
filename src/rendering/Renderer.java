package rendering;

import entities.Camera;
import entities.Entity;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rendering.models.Model;
import rendering.models.TexturedModel;
import rendering.shaders.StaticShader;

public class Renderer
{
    private Camera camera;

    public Renderer(StaticShader shader)
    {
        camera = new Camera(10.0f, 0.1f, 1000.0f);
        camera.setPosition(new Vector3f(45.0f, 100.0f, 100.0f));

        shader.start();
        shader.loadProjectionMatrix(camera.getProjection());
        shader.loadViewMatrix(camera.getView());
        shader.stop();
    }

    public void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
    }

    public void render(Entity entity, StaticShader shader)
    {
        TexturedModel texturedModel = entity.getModel();
        Model model = texturedModel.getModel();

        // Apply the matrices.
        Matrix4f transform = new Matrix4f();
        transform.translate(entity.getPosition());
        transform.scale(entity.getScale());
        transform.rotate(entity.getRotation());
        // end the transform matrix.

        GL30.glBindVertexArray(model.getVaoID());

        // Render the vertex array.
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

            shader.loadTransformationMatrix(transform);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }

}
