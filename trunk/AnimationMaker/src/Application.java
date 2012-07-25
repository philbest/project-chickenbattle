import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class Application implements InputProcessor{
	PerspectiveCamera cam;
	public float camRadius;
	public float camY;
	public Animation animation;
	public ShaderProgram shader;
	public Matrix4 modelViewProjectionMatrix;
	float rightAngle;
	float draggedX;
	float draggedY;
	Sprite background;
	Sprite saveFrame;
	Sprite framebg;
	Sprite play,stopplay, rotate;
	Sprite framebgup;
	Sprite framebgdown;
	SpriteBatch sb;
	BitmapFont font;
	boolean clickedUI;
	float framebgDY;
	int clickedKeyFrame;
	boolean rotating;
	JFileChooser fc;
	public Application() {
		fc = new JFileChooser();
		camY = 0;
		rotating = false;
		sb = new SpriteBatch();
		background = new Sprite(new Texture(Gdx.files.internal("data/background.png")));
		saveFrame = new Sprite(new Texture(Gdx.files.internal("data/saveFrame.png")));
		framebg = new Sprite(new Texture(Gdx.files.internal("data/framebg.png")));
		play = new Sprite(new Texture(Gdx.files.internal("data/play.png")));
		stopplay = new Sprite(new Texture(Gdx.files.internal("data/stopplay.png")));
		rotate = new Sprite(new Texture(Gdx.files.internal("data/rotate.png")));
		framebgup = new Sprite(new Texture(Gdx.files.internal("data/framebgup.png")));
		framebgdown = new Sprite(new Texture(Gdx.files.internal("data/framebgdown.png")));
		font = new BitmapFont();
		modelViewProjectionMatrix = new Matrix4();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camRadius = 5;
		rightAngle = 270;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
		cam.update();

		animation = new Animation();
		shader = new ShaderProgram(Gdx.files.internal("data/simple.vert").readString(),
				Gdx.files.internal("data/simple.frag").readString());
		if (!shader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ shader.getLog());
	}
	public void setInput() {
		Gdx.input.setInputProcessor(this);
	}
	public void changeRadius(float dr) {
		camRadius += dr/2;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
	}
	public void update(){
		cam.update();
	}
	public void render() {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		shader.begin();
		animation.render(this);
		shader.end();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		sb.begin();
		background.setPosition(Gdx.graphics.getWidth()-background.getWidth(),Gdx.graphics.getHeight()-background.getHeight());
		background.draw(sb);
		saveFrame.setPosition(Gdx.graphics.getWidth()-saveFrame.getWidth(),background.getY());
		saveFrame.draw(sb);
		framebg.setPosition(Gdx.graphics.getWidth()-framebg.getWidth(),Gdx.graphics.getHeight()-framebg.getHeight());
		framebg.draw(sb);
		framebgup.setPosition(Gdx.graphics.getWidth()-framebgup.getWidth()-10, framebg.getY()+framebg.getHeight()-framebgup.getHeight()-45);
		framebgup.draw(sb);
		framebgdown.setPosition(Gdx.graphics.getWidth()-framebgup.getWidth()-10, framebg.getY()+10);
		framebgdown.draw(sb);
		play.setPosition(Gdx.graphics.getWidth()-saveFrame.getWidth(),background.getY()+saveFrame.getHeight());
		stopplay.setPosition(Gdx.graphics.getWidth()-saveFrame.getWidth(),background.getY()+saveFrame.getHeight());
		if (animation.playingAnimation)
			stopplay.draw(sb);
		else
			play.draw(sb);
		rotate.setPosition(background.getX(),background.getY());
		if (rotating)
			rotate.draw(sb,0.7f);
		else
			rotate.draw(sb);

		for (int i = 0; i < animation.keyframes.size; i++) {
			if (i == clickedKeyFrame)
				font.setColor(1,0,0,1);
			else
				font.setColor(1,1,1,1);
			font.draw(sb, "KeyFrame " + i, framebg.getX()+15, framebg.getY()+framebg.getHeight()-i*30-50+framebgDY);
		}
		sb.end();
	}

	public void clickedLeft() {
		rightAngle = ((rightAngle-90)+360)%360;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
	}
	public void clickedRight() {
		rightAngle = ((rightAngle+90)+360)%360;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
	}
	public void clickedUp() {
		camY += 1;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
	}
	public void clickedDown() {
		camY -= 1;
		cam.position.set((float)Math.cos(MathUtils.degreesToRadians*rightAngle)*camRadius,camY,(float)Math.sin(MathUtils.degreesToRadians*rightAngle)*camRadius);
		cam.lookAt(0,0,0);
	}
	@Override
	public boolean keyDown(int arg0) {
		if (arg0 == Input.Keys.NUM_1) {
			String cat = JOptionPane.showInputDialog("What file do you want to open?");
			if (!Gdx.files.external(cat+".cpart").exists() || !Gdx.files.external(cat+".ckey").exists()) {
				  JOptionPane.showConfirmDialog(null, "NO SUCH FILE TRY AGAIN");
			} else {
				String f1 = Gdx.files.external(cat+".cpart").readString();
				String f2 = Gdx.files.external(cat+".ckey").readString();
				animation = new Animation(f1,f2);
			}
		} else if (arg0 == Input.Keys.NUM_2) {
			  String cat = JOptionPane.showInputDialog("FileName?");
			  if (Gdx.files.external(cat+".cpart").exists() || Gdx.files.external(cat+".ckey").exists()) {
				  JOptionPane.showConfirmDialog(null, "LOL EXISTS TRY AGAIN");
			  } else {
				  Gdx.files.external(cat+".cpart").writeString(animation.toString(), false);
				  Gdx.files.external(cat+".ckey").writeString(animation.toString2(), false);
			  }
		}
		if (arg0 == Input.Keys.LEFT) {
			clickedLeft();
		} else if (arg0 == Input.Keys.RIGHT) {
			clickedRight();
		} else if (arg0 == Input.Keys.UP) {
			clickedUp();
		} else if (arg0 == Input.Keys.DOWN) {
			clickedDown();
		}
		return false;
	}
	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int arg0) {
		changeRadius(arg0);
		return false;
	}
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if (background.getBoundingRectangle().contains(arg0,Gdx.graphics.getHeight()-arg1)) {
			int y = Gdx.graphics.getHeight()-arg1;
			int x = arg0;
			if (saveFrame.getBoundingRectangle().contains(x,y)) {
				animation.addKeyFrame();
			} else if (play.getBoundingRectangle().contains(x,y)) {
				if (animation.playingAnimation)
					animation.stopAnimation(this);
				else
					animation.playAnimation(this,true);
			} else if (rotate.getBoundingRectangle().contains(x,y)) {
				rotating = !rotating;
			} else if (framebg.getBoundingRectangle().contains(x,y)) {
				if (framebgup.getBoundingRectangle().contains(x,y)) {
					framebgDY += 30;
				} else if (framebgdown.getBoundingRectangle().contains(x,y)) {
					framebgDY -= 30;
				} else {
					TextBounds b = new TextBounds();
					Rectangle rect = new Rectangle();
					for (int i = 0; i < animation.keyframes.size; i++) {
						b = font.getBounds("KeyFrame " + i);
						rect.set(framebg.getX()+15,framebg.getY()+framebg.getHeight()-i*30-b.height-50+framebgDY,b.width,b.height);
						if (rect.contains(x,y)) {
							clickedKeyFrame = i;
							animation.setKeyFrame(animation.keyframes.get(i));
							break;
						}
					}
				}
			}
			clickedUI = true;
			return false;
		}
		Vector3 point = new Vector3(cam.getPickRay(arg0,arg1).origin);
		Vector3 direction = new Vector3(cam.getPickRay(arg0,arg1).direction);
		direction.nor();
		direction.mul(0.5f);
		float range = 0;
		boolean hit = false;
		while (range < 50 && hit == false) {
			range += 0.5f;
			point.add(direction);
			if (animation.clicked(point.x,point.y,point.z)) {
				hit = true;
			}
		}
		draggedX = arg0;
		draggedY = arg1;
		return false;
	}
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		if (clickedUI)
			return false;
		if (rotating) {
			float dx = draggedX-arg0;
			float dy = draggedY-arg1;
			dx /= 100;
			dy /= 100;
			if (rightAngle == 0) {
				animation.selectedPart.rotationX -= dx*10;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 180) {
				animation.selectedPart.rotationX += dx*10;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 90) {
				animation.selectedPart.rotationZ += dx*10;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 270) {
				animation.selectedPart.rotationZ -= dx*10;
				animation.selectedPart.updateModelMatrix();
			}
			draggedX = arg0;
			draggedY = arg1;
		} else {
			float dx = draggedX-arg0;
			float dy = draggedY-arg1;
			dx /= 100;
			dy /= 100;
			if (rightAngle == 0) {
				animation.selectedPart.z += dx;
				animation.selectedPart.y += dy;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 180) {
				animation.selectedPart.z -= dx;
				animation.selectedPart.y += dy;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 90) {
				animation.selectedPart.x -= dx;
				animation.selectedPart.y += dy;
				animation.selectedPart.updateModelMatrix();
			} else if (rightAngle == 270) {
				animation.selectedPart.x += dx;
				animation.selectedPart.y += dy;
				animation.selectedPart.updateModelMatrix();
			}
			draggedX = arg0;
			draggedY = arg1;
		}
		return false;
	}
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		clickedUI = false;
		return false;
	}
}
