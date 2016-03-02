package entities;

import math.Matrix4f;
import math.Vector3f;

public class Camera
{
    private Vector3f position;
    private Vector3f rotation;
    private float pitch;
    private float yaw;
    private float roll;
    private Matrix4f projection;
    private Matrix4f view;
    private float fov;
    private float nearPlane;
    private float farPlane;

    public Camera(float fov, float nearPlane, float farPlane)
    {
        this.fov = fov;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;

        this.projection = new Matrix4f();
        this.view = new Matrix4f();

        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        // Creates a new projection matrix.
        this.projection.projection(fov, nearPlane, farPlane, 800, 600);
        // Creates a new view matrix.
        this.view.view(this.position, this.rotation);
        System.out.println(this.view.toString());
    }

    public void move(Vector3f movement)
    {
        this.position.x += movement.x;
        this.position.y += movement.y;
        this.position.z += movement.z;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getRoll()
    {
        return roll;
    }

    public float getFov()
    {
        return fov;
    }

    public void setFov(float fov)
    {
        this.fov = fov;
    }

    public float getNearPlane()
    {
        return nearPlane;
    }

    public void setNearPlane(float nearPlane)
    {
        this.nearPlane = nearPlane;
    }

    public float getFarPlane()
    {
        return farPlane;
    }

    public void setFarPlane(float farPlane)
    {
        this.farPlane = farPlane;
    }

    public Matrix4f getProjection()
    {
        return this.projection;
    }

    public Matrix4f getView()
    {
        return this.view;
    }
}
