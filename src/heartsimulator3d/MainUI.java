/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainUI.java
 *
 * Created on 18-Jul-2009, 13:55:15
 */
package heartsimulator3d;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import vtk.vtkActor;
import vtk.vtkAlgorithmOutput;
import vtk.vtkAxesActor;
import vtk.vtkCamera;
import vtk.vtkContourFilter;
import vtk.vtkPolyDataMapper;
import vtk.vtkPolyDataNormals;
import vtk.vtkStructuredPointsReader;

/**
 *
 * @author Lee Boynton
 */
public class MainUI extends javax.swing.JFrame
{
    private final String file = "HeartImage.vtk";
    private vtkAlgorithmOutput output;
    private vtkCamera camera;

    /** Creates new form MainUI */
    public MainUI()
    {
        initComponents();
        setLocationRelativeTo(null);
        
        try
        {
            readVtkFile();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setup3DRender();
        showPosition();
    }

    private void setup3DRender()
    {
        // Add in some surface geometry for interest.
        vtkContourFilter iso = new vtkContourFilter();
        iso.SetInputConnection(output);
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
        pnlRender.GetRenderer().AddActor(axes);

        // Add actor in 3D Panel
        pnlRender.GetRenderer().AddActor(isoActor);

        camera = pnlRender.GetRenderer().GetActiveCamera();
        camera.SetFocalPoint(50, 50, 50);
        camera.SetPosition(-10, -12, -164);
        camera.SetViewUp(-1, 0, 0);

        //pnlRender.Render();
    }

    private void readVtkFile() throws FileNotFoundException, Exception
    {
        System.out.println("Reading VTK file: " + file);

        // check file exists
        if(!new File(file).isFile())
        {
            throw new FileNotFoundException("Could not read VTK file: " + file);
        }
        vtkStructuredPointsReader reader = new vtkStructuredPointsReader();
        reader.SetFileName(file);
        if(reader.IsFileStructuredPoints() == 0)
        {
            throw new Exception("VTK file does not contain structured points data");
        }

        output = reader.GetOutputPort();
    }

    private void showPosition()
    {
        lblPosition.setText("X: " + camera.GetPosition()[0] + " Y: " + camera.GetPosition()[1] + " Z: " + camera.GetPosition()[2]);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlStatus = new javax.swing.JPanel();
        lblPosition = new javax.swing.JLabel();
        pnlRender = new vtk.vtkPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblPosition.setText("Camera position");

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addComponent(lblPosition)
                .addContainerGap(502, Short.MAX_VALUE))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPosition)
        );

        pnlRender.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlRenderMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlRender, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlRender, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlRenderMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pnlRenderMouseDragged
    {//GEN-HEADEREND:event_pnlRenderMouseDragged
        showPosition();
    }//GEN-LAST:event_pnlRenderMouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new MainUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblPosition;
    private vtk.vtkPanel pnlRender;
    private javax.swing.JPanel pnlStatus;
    // End of variables declaration//GEN-END:variables
}
