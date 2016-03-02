package rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import de.matthiasmann.twl.utils.PNGDecoder;
import rendering.models.Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader
{
    // We store a list of the different units that we use.
    // This is so we can easily clean up the resources.
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    // Loads up the model data into memory based on the
    // data passed into it.
    public Model load(float[] position, int[] indices, float[] textureCoords)
    {
        // Binds the model data into a Vertex Array Object.
        int vaoID = this.createVAO();

        // Store Vertex Position Data.
        this.bindIndicesBuffer(indices);

        // Stores vertex position into AttributeList 0.
        this.storeDataInAttributeList(0, 3, position);

        // Stores texture UV data into attribute list 1.
        this.storeDataInAttributeList(1, 2, textureCoords);

        // Unbinds the Vertex Array Object.
        this.unbindVAO();

        return new Model(vaoID, indices.length);
    }

    // Loads texture data into our model memory.
    public int loadTexture(String fileName)
    {
        int textureID = 0;

        try {
            InputStream in = new FileInputStream("res/"+fileName+".png");
            PNGDecoder decoder = new PNGDecoder(in);

            ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
            decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            buf.flip();

            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            // WRAP OR NOT TO WRAP, THAT IS THE QUESTION
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            // ME DOEST SMOOTH OR SHARP? SHARP!
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11. GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            // MIP MAP!
            GL30.glGenerateMipmap(textureID);

            // MAKE!
            GL11. glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();

        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return textureID;
    }


    // Clean all this shite up
    public void clean()
    {
        for(int vao:vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for(int vbo:vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for(int tex:textures) {
            GL11.glDeleteTextures(tex);
        }
    }

    // Creates our vertex array object for loading data into memory.
    private int createVAO()
    {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);

        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    // Stores some data into our attribute list.
    private void storeDataInAttributeList(int attributeNumber, int size, float[] data)
    {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = this.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO()
    {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices)
    {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = this.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
