package entities;

import math.Vector3f;
import rendering.models.TexturedModel;

public class Entity
{
    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Entity(TexturedModel model, Vector3f positon, Vector3f rotation, Vector3f scale)
    {
        this.model = model;
        this.position = positon;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void move(Vector3f movement)
    {
        this.position.x += movement.x;
        this.position.y += movement.y;
        this.position.z += movement.z;
    }

    public void rotate(Vector3f rotate)
    {
        this.rotation.x += rotate.x;
        this.rotation.y += rotate.y;
        this.rotation.z += rotate.z;
    }

    public void scale(Vector3f scale)
    {
        this.scale.x += scale.x;
        this.scale.y += scale.y;
        this.scale.z += scale.z;
    }

    public TexturedModel getModel()
    {
        return model;
    }

    public void setModel(TexturedModel model)
    {
        this.model = model;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f positon)
    {
        this.position = positon;
    }

    public Vector3f getRotation()
    {
        return rotation;
    }

    public void setRotation(Vector3f rotation)
    {
        this.rotation = rotation;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }
}
