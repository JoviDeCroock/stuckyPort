/*
 *  SimpleRenderer.java
 *  ARToolKit5
 *
 *  Disclaimer: IMPORTANT:  This Daqri software is supplied to you by Daqri
 *  LLC ("Daqri") in consideration of your agreement to the following
 *  terms, and your use, installation, modification or redistribution of
 *  this Daqri software constitutes acceptance of these terms.  If you do
 *  not agree with these terms, please do not use, install, modify or
 *  redistribute this Daqri software.
 *
 *  In consideration of your agreement to abide by the following terms, and
 *  subject to these terms, Daqri grants you a personal, non-exclusive
 *  license, under Daqri's copyrights in this original Daqri software (the
 *  "Daqri Software"), to use, reproduce, modify and redistribute the Daqri
 *  Software, with or without modifications, in source and/or binary forms;
 *  provided that if you redistribute the Daqri Software in its entirety and
 *  without modifications, you must retain this notice and the following
 *  text and disclaimers in all such redistributions of the Daqri Software.
 *  Neither the name, trademarks, service marks or logos of Daqri LLC may
 *  be used to endorse or promote products derived from the Daqri Software
 *  without specific prior written permission from Daqri.  Except as
 *  expressly stated in this notice, no other rights or licenses, express or
 *  implied, are granted by Daqri herein, including but not limited to any
 *  patent rights that may be infringed by your derivative works or by other
 *  works in which the Daqri Software may be incorporated.
 *
 *  The Daqri Software is provided by Daqri on an "AS IS" basis.  DAQRI
 *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE, REGARDING THE DAQRI SOFTWARE OR ITS USE AND
 *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 *  IN NO EVENT SHALL DAQRI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 *  MODIFICATION AND/OR DISTRIBUTION OF THE DAQRI SOFTWARE, HOWEVER CAUSED
 *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 *  STRICT LIABILITY OR OTHERWISE, EVEN IF DAQRI HAS BEEN ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.samples.ARSimple;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.MediaStore;
import android.util.Log;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class SimpleRenderer extends ARRenderer implements SingleObservable{

    private HashMap<Integer,Object3D> markerModelMap;
    private int[] markers;
    private boolean[] foundmarkers;
    private World world;
    private Light sun;
    private Camera camera;
    private Context context;
    private FrameBuffer frameBuffer;


    private Matrix projMatrix=new Matrix(); //the matrix having the camera projection
    private Matrix markerMatrix=new Matrix(); //the matrix having the transformation to place the tank in the marker
    private Matrix modelMatrix= new Matrix(); //the matrix having the model rotation and translation

    private SingleObserver observer;

    public SimpleRenderer(Context ctx){

        this.context = ctx;
        this.markerModelMap = new HashMap<>();
        markers = new int[4];
        ARToolKit.getInstance().addMarker("single;Data/hiro.patt;80");
        ARToolKit.getInstance().addMarker("single;Data/kanji.patt;80");
        ARToolKit.getInstance().addMarker("single;Data/two.patt;80");
        foundmarkers =  new boolean[4];
        Arrays.fill(foundmarkers,true);

    }

    public void bindModelToMarker(int markerId, InputStream input) throws IOException{

        if(markerId < 1){
            return;
        }

        Object3D model = Object3D.mergeAll(Loader.load3DS(input, 5));
        model.strip();
        model.build();

        markerModelMap.put(markerId-1,model);
        foundmarkers[markerId -1] = false;
    }

    @Override
    public boolean configureARScene() {
        configureJPCTWorld();

        if(markerModelMap.keySet().size() < 0)
            return false;

        return true;
    }

    public void configureJPCTWorld(){
        world=new World();
        world.setAmbientLight(20, 20, 20);

        sun= new Light(world);
        sun.setIntensity(250, 250, 250);

        camera = world.getCamera();

        Texture tankTexture= new Texture(
                BitmapHelper.rescale(
                        BitmapHelper.convert(
                                context.getResources().getDrawable(R.drawable.tanktexture)
                                ),128,128
                        )
                );
        TextureManager.getInstance().addTexture("theTankTexture",tankTexture);

    }


    @Override
    public void onSurfaceChanged(GL10 unused, int w, int h) {
        super.onSurfaceChanged(unused, w, h);
        if(frameBuffer!=null){
            frameBuffer.dispose();
        }
        frameBuffer = new FrameBuffer(unused,w,h);
    }

    public boolean foundAllMArkers(){
        return !Arrays.asList(foundmarkers).contains(false);
    }

    @Override
    public void draw(GL10 gl) {

        world.removeAllObjects();

        float[] projectionCamera=ARToolKit.getInstance().getProjectionMatrix();

        projMatrix.setIdentity();

        projMatrix.setDump(projectionCamera);
        projMatrix.transformToGL();
        SimpleVector translation=projMatrix.getTranslation();
        SimpleVector dir=projMatrix.getZAxis();
        SimpleVector up=projMatrix.getYAxis();
        camera.setPosition(translation);
        camera.setOrientation(dir, up);

        for (int marker: markerModelMap.keySet()) {
            if(ARToolKit.getInstance().queryMarkerVisible(marker)){

                //foundmarkers[Arrays.asList(markers).indexOf(marker)] = true;
                if(foundAllMArkers()){
                    updateObserver();
                }
                //Reset the matrix
                markerMatrix.setIdentity();
                modelMatrix.setIdentity();
                Object3D object = markerModelMap.get(marker);
                object.clearTranslation();
                object.clearRotation();

                //do the model transformations (rotate first and translate then)
                //the angle must be negative, or x-y movement swapped, as JPCT has an inverted z-axis

                modelMatrix.rotateZ((float) Math.toRadians(-180+180));
                modelMatrix.translate(20, 0, 0);

                //the position and rotation of the marker gives us the transformation matrix
                float[] markerTransformation=
                        ARToolKit
                                .getInstance()
                                .queryMarkerTransformation(marker);

                markerMatrix.setDump(markerTransformation);
                markerMatrix.transformToGL();

                //now multiply trasnformationMat * modelMat

                modelMatrix.matMul(markerMatrix);

                //apply the resulting matrix to the tank.

                object.setRotationMatrix(modelMatrix);
                object.setTranslationMatrix(modelMatrix);

                world.addObject(object);
            }
        }

        frameBuffer.clear();
        world.renderScene(frameBuffer);
        world.draw(frameBuffer);
        frameBuffer.display();

    }

    @Override
    public void setObserver(SingleObserver obj) {
        this.observer = obj;
    }

    @Override
    public void updateObserver() {
        this.observer.update(this);
    }
}