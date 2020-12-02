import java.awt.Graphics;
import javax.swing.JFrame;
import java.lang.Math;

public class Cube
{
    public static void main(String[] args)
    {
        new Cube("randomly rotating 3d cube", 600, 600);
    }

    // window properties
    private int w;
    private int h;

    // cube properties
    private float xRot = (float)Math.PI/180;
    private float yRot = (float)Math.PI/180;
    private float zRot = (float)Math.PI/180;

    private float[][] rotationXMatrix = new float[3][3]; // 3x3 matrix to rotate 3d coordinates along X axis
    private float[][] rotationYMatrix = new float[3][3]; // 3x3 matrix to rotate 3d coordinates along Y axis
    private float[][] rotationZMatrix = new float[3][3]; // 3x3 matrix to rotate 3d coordinates along Z axis

    private float[][] vertices = { // collection of 3d coordinates relative to center of the window (3x8 matrix)
                                    {100, 100, 100}, {100, 100, -100}, {100, -100, 100}, {-100, 100, 100}, 
                                    {-100, 100, -100}, {-100, -100, 100}, {-100, -100, -100}, {100, -100, -100}
                                };

    Cube(String title, int width, int height)
    {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);

        Graphics g = frame.getGraphics();

        start(g, frame);
    }

    private float[][] multiplyMatrices(float[][] m1, float[][] m2)
    {
        float[][] multipliedMatrix = new float[m2.length][m1[0].length];

        if (m1.length != m2[0].length)
            return multipliedMatrix;

        for (int row = 0; row < m1[0].length; row++)
        {
            for (int col = 0; col < m2.length; col++)
            {
                for (int i = 0; i < m1.length; i++)
                {
                    multipliedMatrix[col][row] += m1[i][row] * m2[col][i];
                }
            }
        }

        return multipliedMatrix;
    }

    private void start(Graphics g, JFrame f)
    {
        while (true)
        {
            // update window dimensions
            w = f.getBounds().width;
            h = f.getBounds().height;

            // clear previous frame
            g.clearRect(0, 0, w, h);

            // randomly change rotation speed
            if (Math.random() > 0.5)
            {
                xRot += (float)Math.PI * (1 / (Math.random() * 1000 + 300));
                yRot += (float)Math.PI * (1 / (Math.random() * 1000 + 300));
                zRot += (float)Math.PI * (1 / (Math.random() * 1000 + 300));
            }
            else
            {
                xRot -= (float)Math.PI * (1 / (Math.random() * 1000 + 300));
                yRot -= (float)Math.PI * (1 / (Math.random() * 1000 + 300));
                zRot -= (float)Math.PI * (1 / (Math.random() * 1000 + 300));
            }

            // clamp rotation speed
            if (xRot > Math.PI/90)
                xRot = (float)Math.PI/90;
            if (yRot > Math.PI/90)
                yRot = (float)Math.PI/90;
            if (zRot > Math.PI/90)
                zRot = (float)Math.PI/90;

            if (xRot < Math.PI/360)
                xRot = (float)Math.PI/360;
            if (yRot < Math.PI/360)
                yRot = (float)Math.PI/360;
            if (zRot < Math.PI/360)
                zRot = (float)Math.PI/360;

            // rotate coordinates and render new cube
            vertices = rotate(vertices, xRot, yRot, zRot);
            draw(g);

            // wait before another loop
            try
            {
                Thread.sleep(1000/20);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    private float[][] rotate(float[][] m, float xDeg, float yDeg, float zDeg)
    {
        rotationXMatrix[0][0] = 1;
        rotationXMatrix[0][1] = 0;
        rotationXMatrix[0][2] = 0;
        rotationXMatrix[1][0] = 0;
        rotationXMatrix[1][1] = (float)Math.cos(xDeg);
        rotationXMatrix[1][2] = (float)Math.sin(xDeg);
        rotationXMatrix[2][0] = 0;
        rotationXMatrix[2][1] = -1* (float)Math.sin(xDeg);
        rotationXMatrix[2][2] = (float)Math.cos(xDeg);

        rotationYMatrix[0][0] = (float)Math.cos(yDeg);
        rotationYMatrix[0][1] = 0;
        rotationYMatrix[0][2] = (float)Math.sin(yDeg);
        rotationYMatrix[1][0] = 0;
        rotationYMatrix[1][1] = 1;
        rotationYMatrix[1][2] = 0;
        rotationYMatrix[2][0] = -1 * (float)Math.sin(yDeg);;
        rotationYMatrix[2][1] = 0;
        rotationYMatrix[2][2] = (float)Math.cos(yDeg);;

        rotationZMatrix[0][0] = (float)Math.cos(zDeg);
        rotationZMatrix[0][1] = (float)Math.sin(zDeg);
        rotationZMatrix[0][2] = 0;
        rotationZMatrix[1][0] = -1 * (float)Math.sin(zDeg);
        rotationZMatrix[1][1] = (float)Math.cos(zDeg);
        rotationZMatrix[1][2] = 0;
        rotationZMatrix[2][0] = 0;
        rotationZMatrix[2][1] = 0;
        rotationZMatrix[2][2] = 1;

        return multiplyMatrices(rotationZMatrix, multiplyMatrices(rotationYMatrix, multiplyMatrices(rotationXMatrix, m)));
    }

    private void draw(Graphics g)
    {
        // draw a line from each vertex to each other vertex
        for (int i = 0; i < vertices.length; i++)
        {
            for (int j = 0; j < vertices.length; j++)
            {
                g.drawLine((int)((w/2) + vertices[i][0]), (int)((h/2) + vertices[i][1]), 
                    (int)((w/2) + vertices[j][0]), (int)((h/2) + vertices[j][1]));
            }
        }
    }
}