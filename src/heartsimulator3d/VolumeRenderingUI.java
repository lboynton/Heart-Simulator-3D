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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import vtk.vtkAlgorithmOutput;
import vtk.vtkAxesActor;
import vtk.vtkCamera;
import vtk.vtkColorTransferFunction;
import vtk.vtkPiecewiseFunction;
import vtk.vtkRenderWindow;
import vtk.vtkStructuredPointsReader;
import vtk.vtkVolume;
import vtk.vtkVolumeProperty;
import vtk.vtkVolumeTextureMapper2D;

/**
 *
 * @author Lee Boynton
 */
public class VolumeRenderingUI extends javax.swing.JFrame
{
    private final String file = "HeartImage.vtk";
    private final double cameraPosition[] =
    {
        -10, -12, -164
    };
    private vtkAlgorithmOutput output;
    private vtkCamera camera;

    /** Creates new form MainUI */
    public VolumeRenderingUI()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println("Unable to use system look and feel");
        }

        initComponents();
        setLocationRelativeTo(null);

        try
        {
            readVtkFile();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setup3DRender();
        showPosition();
    }

    private void setup3DRender()
    {
        // 2D texture mapping
        // Create transfer mapping scalar value to opacity
        vtkPiecewiseFunction opacityTransferFunction = new vtkPiecewiseFunction();
        opacityTransferFunction.AddPoint(20, 0.0);
        opacityTransferFunction.AddPoint(255, 1);

        // Create transfer mapping scalar value to color
        vtkColorTransferFunction colorTransferFunction = new vtkColorTransferFunction();

        // use with different opacities
        colorTransferFunction.AddRGBPoint(0.0, 0.0, 0.0, 0.0);
        colorTransferFunction.AddRGBPoint(64.0, 1.0, 0.0, 0.0);
        colorTransferFunction.AddRGBPoint(128.0, 0.0, 0.0, 1.0);
        colorTransferFunction.AddRGBPoint(192.0, 0.0, 1.0, 0.0);
        colorTransferFunction.AddRGBPoint(255.0, 0.0, 0.2, 0.0);

        // heart coloured
        // use with data without different opacities
        colorTransferFunction.AddRGBPoint(255.0, 111, 227, 214);

        // The property describes how the data will look
        vtkVolumeProperty volumeProperty = new vtkVolumeProperty();
        volumeProperty.SetColor(colorTransferFunction);
        volumeProperty.SetScalarOpacity(opacityTransferFunction);

        vtkVolumeTextureMapper2D volumeMapper = new vtkVolumeTextureMapper2D();
        volumeMapper.SetInputConnection(output);

        vtkVolume volume = new vtkVolume();
        volume.SetMapper(volumeMapper);
        volume.SetProperty(volumeProperty);
        pnlRender.GetRenderer().AddVolume(volume);

        // add in axes
        vtkAxesActor axes = new vtkAxesActor();
        axes.SetTotalLength(10, 10, 10);
        pnlRender.GetRenderer().AddActor(axes);

        camera = pnlRender.GetRenderer().GetActiveCamera();
        camera.SetFocalPoint(50, 50, 50);
        camera.SetPosition(cameraPosition);
        camera.SetViewUp(-1, 0, 0);

        pnlRender.Render();
        pnlRender.addWindowSetObserver(new Observer()
        {
            public void update(Observable o, Object arg)
            {
                vtkRenderWindow renWin = (vtkRenderWindow) arg;
                if (renWin.GetEventPending() != 0)
                {
                    renWin.SetAbortRender(1);
                }
            }
        });
    }

    private void readVtkFile() throws FileNotFoundException, Exception
    {
        System.out.println("Reading VTK file: " + file);

        // check file exists
        if (!new File(file).isFile())
        {
            throw new FileNotFoundException("Could not read VTK file: " + file);
        }
        vtkStructuredPointsReader reader = new vtkStructuredPointsReader();
        reader.SetFileName(file);
        if (reader.IsFileStructuredPoints() == 0)
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
        toolbar = new javax.swing.JToolBar();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Heart Volume Rendering");

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

        toolbar.setRollover(true);

        btnReset.setText("Reset view");
        btnReset.setFocusable(false);
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        toolbar.add(btnReset);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
            .addComponent(pnlRender, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlRender, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlRenderMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pnlRenderMouseDragged
    {//GEN-HEADEREND:event_pnlRenderMouseDragged
        showPosition();
    }//GEN-LAST:event_pnlRenderMouseDragged

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        camera.SetPosition(cameraPosition);
        camera.SetViewUp(-1, 0, 0);
        pnlRender.UpdateLight();
        pnlRender.Render();
        showPosition();
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new VolumeRenderingUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JLabel lblPosition;
    private vtk.vtkPanel pnlRender;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables
}