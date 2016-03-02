import static org.lwjgl.glfw.GLFW.*;

import entities.Entity;
import math.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.*;

import rendering.*;
import rendering.models.Model;
import rendering.models.TexturedModel;
import rendering.Texture;
import rendering.shaders.StaticShader;

public class Game
{
    private int width = 800;
    private int height = 600;

    private long window;

    private ModelLoader loader;
    private Renderer renderer;
    StaticShader shader;
    private Entity entity;

    float[] vertices = {
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,0.5f,-0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f
    };

    int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
    };

    float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0
    };

    Model model;
    Texture texture;
    TexturedModel texturedModel;

    private GLFWKeyCallback keyCallback;

    public Game()
    {

    }

    public void run()
    {
        this.init();

        while ( glfwWindowShouldClose(this.window) == GLFW_FALSE ) {
            this.update();
            this.draw();
        }

        this.shutdown();
    }

    // Creates a new window.
    private void init()
    {
        this.createContextDisplay();

        loader = new ModelLoader();

        this.shader = new StaticShader();
        this.model = loader.load(this.vertices, this.indices, this.textureCoords);
        this.texture = new Texture(loader.loadTexture("test"));
        this.texturedModel = new TexturedModel(this.model, this.texture);

        this.entity = new Entity(texturedModel, new Vector3f(0.0f, 0, -20.0f),
                                                new Vector3f(30.0f, 45.0f, 0.0f),
                                                new Vector3f(1.0f, 1.0f, 1.0f));

        renderer = new Renderer(shader);
    }

    // Update game
    private void update()
    {
        this.entity.rotate(new Vector3f(0f, 1.0f,0));
        glfwPollEvents();
    }

    // Draw frame
    private void draw()
    {
        this.renderer.prepare();

        this.shader.start();
        this.renderer.render(this.entity, shader);
        this.shader.stop();

        glfwSwapBuffers(this.window);
    }

    // Shut this shit down
    private void shutdown()
    {
        this.shader.clean();
        this.loader.clean();
    }

    // create a window and attach opengl context.
    private void createContextDisplay()
    {
        if ( glfwInit() != GLFW_TRUE ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        this.window = glfwCreateWindow(this.width, this.height, "Terminus", 0, 0);

        if ( this.window  == 0 ) {
            throw new RuntimeException("Failed to create the window!");
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                this.window,
                (vidMode.width() - this.width) / 2,
                (vidMode.height() - this.height) / 2
        );

        glfwMakeContextCurrent(this.window);
        glfwShowWindow(this.window);

        GL.createCapabilities();
    }
}
