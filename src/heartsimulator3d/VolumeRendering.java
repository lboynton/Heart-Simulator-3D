/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartsimulator3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import vtk.vtkActor;
import vtk.vtkAxesActor;
import vtk.vtkCamera;
import vtk.vtkContourFilter;
import vtk.vtkPanel;
import vtk.vtkPolyDataMapper;
import vtk.vtkPolyDataNormals;
import vtk.vtkStructuredPointsReader;

/**
 *
 * @author Lee Boynton
 */
public class VolumeRendering
{
    public static void main(String args[])
    {
        vtkPanel panel3D = new vtkPanel();
        vtkStructuredPointsReader reader = new vtkStructuredPointsReader();
        reader.SetFileName("HeartImage.vtk");

        // Add in some surface geometry for interest.
        vtkContourFilter iso = new vtkContourFilter();
        iso.SetInputConnection(reader.GetOutputPort());
        iso.SetValue(0, .22);
        vtkPolyDataNormals normals = new vtkPolyDataNormals();
        normals.SetInputConnection(iso.GetOutputPort());
        normals.SetFeatureAngle(45);
        vtkPolyDataMapper isoMapper = new vtkPolyDataMapper();
        isoMapper.SetInputConnection(normals.GetOutputPort());
        isoMapper.ScalarVisibilityOff();
        vtkActor isoActor = new vtkActor();
        isoActor.SetMapper(isoMapper);
        isoActor.GetProperty().SetDiffuseColor(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue());
        isoActor.GetProperty().SetSpecularColor(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue());
        isoActor.GetProperty().SetDiffuse(.8);
        isoActor.GetProperty().SetSpecular(.5);
        isoActor.GetProperty().SetSpecularPower(30);

        // add in axes
        vtkAxesActor axes = new vtkAxesActor();
        axes.SetTotalLength(10, 10, 10);
        panel3D.GetRenderer().AddActor(axes);

        // Add actor in 3D Panel
        panel3D.GetRenderer().AddActor(isoActor);

        // Position camera
        final vtkCamera camera = panel3D.GetRenderer().GetActiveCamera();
        camera.SetFocalPoint(50, 50, 50);
        camera.SetPosition(0, -12, -164);
        camera.SetViewUp(-1, 0, 0);

        panel3D.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mousePressed(MouseEvent e)
            {
                System.out.println("X: " + camera.GetPosition()[0] + " Y: " + camera.GetPosition()[1] + " Z: " + camera.GetPosition()[2]);
                System.out.println(camera.GetViewAngle());
                System.out.println(camera.GetFocalPoint()[0] + ", " + camera.GetFocalPoint()[0] + ", " + camera.GetFocalPoint()[0]);
                System.out.println(camera.GetViewUp()[0] + ", " + camera.GetViewUp()[1] + ", " + camera.GetViewUp()[2]);
            }

            public void mouseReleased(MouseEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseExited(MouseEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        // Build Java Frame and include the VTK view
        JFrame frame = new JFrame("Heart Volume Rendering");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel3D, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        // Center on desktop
        frame.setVisible(true);
    }
}
