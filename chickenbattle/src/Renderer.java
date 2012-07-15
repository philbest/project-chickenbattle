

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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
	ShaderProgram particleShader;
	Sprite crosshair;
	Sprite gun;
	Sprite block;
	SpriteBatch sb;
	public Renderer() {
		sb = new SpriteBatch();
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
		gun = new Sprite(new Texture(Gdx.files.internal("data/gun.png")));
		block = new Sprite(new Texture(Gdx.files.internal("data/block.png")));
		simpleShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/simple.vert").readString(), Gdx.files.internal(
		"data/shaders/simple.frag").readString());
		if (!simpleShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ simpleShader.getLog());

		particleShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/particleShader.vert").readString(), Gdx.files.internal(
		"data/shaders/particleShader.frag").readString());
		if (!particleShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ particleShader.getLog());

		cubeTexture = new Texture(Gdx.files.internal("data/grassmap.png"));
		lightTexture = new Texture(Gdx.files.internal("data/light.png"));
	}
	public void render(Application app) {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		renderMapChunks(app);
		renderLights(app);
		renderVector(app.from,app.to,app);

		crosshair.setPosition(Gdx.graphics.getWidth()/2-crosshair.getWidth()/2,Gdx.graphics.getHeight()/2-crosshair.getHeight()/2);
		gun.setPosition(Gdx.graphics.getWidth()-gun.getWidth(), 0);
		block.setPosition(Gdx.graphics.getWidth()-block.getWidth(), 0);

		for (int i = 0; i < app.map.chunks.size;i++) {
			if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0) {
				this.renderBoundingBox(app,app.map.chunks.get(i).bounds);
			}
		}
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		if (!app.adding)
			crosshair.setColor(1,0,0,1);
		else
			crosshair.setColor(1,1,1,1);
		sb.begin();
		crosshair.draw(sb);
		if (!app.adding)
			gun.draw(sb);
		else
			block.draw(sb);
		sb.end();
	}
	public void renderMapChunks(Application app) {
		cubeTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniform4fv("scene_light", app.light.color, 0,4);
		simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
//		Vector3 temp = new Vector3();
//		for (int i = 0; i < app.map.chunks.size; i++) {
//			Chunk c = app.map.chunks.get(i);
//			temp.set(c.x,c.y,c.z);
//			temp.sub(app.cam.position);
//			c.distance = temp.len();
//		}
		//app.map.chunks.sort();
		int vertices = 0;
		for (int i = 0; i < app.map.chunks.size;i++) {
			if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
				//if (app.map.chunks[x][y][z].chunkMesh != null && app.map.chunks[x][y][z].chunkMesh.getNumVertices() > 0) {
				simpleShader.setUniformi("s_texture", 0);
				cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);
				
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
				app.map.chunks.get(i).chunkMesh.render(simpleShader, GL20.GL_TRIANGLES);
				vertices+=app.map.chunks.get(i).chunkMesh.getNumVertices();
			}
		}
		System.out.println("Vertices: " + vertices);
		simpleShader.end();
	}
	public void renderBoundingBox(Application app, BoundingBox b) {
		Vector3[] c = b.getCorners();
		float[] vertices = new float[24];
		int i = 0;

		vertices[0] = c[0].x; vertices[1] = c[0].y; vertices[2] = c[0].z;
		vertices[3] = c[1].x; vertices[4] = c[1].y; vertices[5] = c[1].z;
		vertices[6] = c[2].x; vertices[7] = c[2].y; vertices[8] = c[2].z;
		vertices[9] = c[3].x; vertices[10] = c[3].y; vertices[11] = c[3].z; 
		vertices[12] = c[4].x; vertices[13] = c[4].y;
		vertices[14] = c[4].z; vertices[15] = c[5].x; vertices[16] =c[5].y;
		vertices[17] = c[5].z; vertices[18] = c[6].x;
		vertices[19] = c[6].y; vertices[20] = c[6].z; 
		vertices[21] = c[7].x; vertices[22] = c[7].y; vertices[23] = c[7].z;
		short[] indices = new short[17];
		indices[0] = 3; indices[1] = 2; indices[2] = 1; indices[3] = 5;
		indices[4] = 6; indices[5] = 2; indices[6] = 3; indices[7] = 7;
		indices[8] = 4; indices[9] = 5; indices[10] = 6; indices[11] = 7;
		indices[12] = 4; indices[13] = 0; indices[14] = 3; indices[15] =0; indices[16] = 1;
		Mesh vectorTest = new Mesh(true,8,17,new VertexAttribute(Usage.Position, 3,"a_position"));
		particleShader.begin(); 
		vectorTest.setVertices(vertices);
		vectorTest.setIndices(indices);

		modelViewProjectionMatrix.set(app.cam.combined);
		particleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		vectorTest.render(particleShader, GL20.GL_LINE_STRIP);
		particleShader.end();
		vectorTest.dispose();
	}
	public void renderMap(Application app) {
		cubeTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniform4fv("scene_light", app.light.color, 0,4);
		simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
		for (int x = 0; x < Map.x; x++) {
			for (int y = 0; y < Map.y; y++) {
				for (int z = 0; z < Map.z; z++) {
					if (app.map.map[x][y][z] == 1) {
						simpleShader.setUniformi("s_texture", 0);
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
						Cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
					}
				}
			}
		}
		simpleShader.end();
	}
	public void renderLights(Application app) {
		lightTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniformi("s_texture", 0);
		cubeModel.setToTranslation(app.light.posX,app.light.posY,app.light.posZ);
		modelViewProjectionMatrix.set(app.cam.combined);
		modelViewProjectionMatrix.mul(cubeModel);
		simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
		app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
		simpleShader.end();
	}

	public void renderVector(Vector3 from,Vector3 to, Application app) {
		Mesh vectorTest = new Mesh(true,2,0,new VertexAttribute(Usage.Position, 3,"a_position"));
		particleShader.begin();
		float[] vertices = new float[]{ from.x,from.y,from.z,to.x,to.y,to.z}; 
		vectorTest.setVertices(vertices);


		Matrix4 modelViewProjectionMatrix = new Matrix4();
		modelViewProjectionMatrix.set(app.cam.combined);

		particleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		vectorTest.render(particleShader, GL20.GL_LINES);
		particleShader.end();
		vectorTest.dispose();
	}
}
