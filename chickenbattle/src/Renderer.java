

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;



public class Renderer {
	public Mesh cube;
	Matrix4 cubeModel = new Matrix4();
	Matrix4 cubeModel2 = new Matrix4();
	Matrix4 modelViewProjectionMatrix = new Matrix4();
	Matrix4 modelViewMatrix = new Matrix4();
	Matrix3 normalMatrix = new Matrix3();
	Texture cubeTexture;
	Texture lightTexture;
	ShaderProgram simpleShader;
	public Renderer() {
		simpleShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/simple.vert").readString(), Gdx.files.internal(
		"data/shaders/simple.frag").readString());
		if (!simpleShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ simpleShader.getLog());
		cubeTexture = new Texture(Gdx.files.internal("data/grassmap.png"));
		lightTexture = new Texture(Gdx.files.internal("data/light.png"));
	}
	public void render(Application app) {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		renderMap(app);
		renderLights(app);
	}
	public void renderMap(Application app) {
		app.cam.update();
		cubeTexture.bind(1);
		simpleShader.begin();
		simpleShader.setUniform4fv("scene_light", app.light.color, 0,4);
		simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
		for (int x = 0; x < Map.x; x++) {
			for (int y = 0; y < Map.y; y++) {
				for (int z = 0; z < Map.z; z++) {
					if (app.map.map[x][y][z] != null) {
						simpleShader.setUniformi("s_texture", 1);
						cubeModel.setToTranslation(x,y,z);
						modelViewProjectionMatrix.set(app.cam.combined);
						modelViewProjectionMatrix.mul(cubeModel);
						modelViewMatrix.set(app.cam.view);
						modelViewMatrix.mul(cubeModel);
						normalMatrix.set(modelViewMatrix);
						simpleShader.setUniformMatrix("normalMatrix", normalMatrix);
						simpleShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
						simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
						simpleShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
						simpleShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
						simpleShader.setUniformf("material_shininess", 0.5f);
						simpleShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);
						app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
					}
				}
			}
		}
		simpleShader.end();
	}
	public void renderLights(Application app) {
		lightTexture.bind(1);
		simpleShader.begin();
		simpleShader.setUniformi("s_texture", 1);
		cubeModel.setToTranslation(app.light.posX,app.light.posY,app.light.posZ);
		modelViewProjectionMatrix.set(app.cam.combined);
		modelViewProjectionMatrix.mul(cubeModel);
		simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
		app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
		simpleShader.end();
	}
}
