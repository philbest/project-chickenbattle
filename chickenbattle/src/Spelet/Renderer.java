package Spelet;



import java.io.IOException;
import java.io.InputStream;

import Map.Map;
import Screens.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class Renderer {
	public Mesh cube;
	Mesh skysphere;
	Matrix4 cubeModel = new Matrix4();
	Matrix4 cubeModel2 = new Matrix4();
	Matrix4 skysphereModel = new Matrix4();

	Matrix4 modelViewProjectionMatrix = new Matrix4();
	Matrix4 modelViewMatrix = new Matrix4();
	Matrix3 normalMatrix = new Matrix3();

	Texture cubeTexture;
	Texture lightTexture;
	Texture skysphereTexture;

	ShaderProgram simpleShader;
	ShaderProgram particleShader;
	ShaderProgram skysphereShader;

	SpriteBatch sb;
	FrameBuffer shadowMap;
	ShaderProgram shadowGenShader;
	ShaderProgram shadowMapShader;
	int renderMode;
	PerspectiveCamera lightCam;
	public void initiateShadows() {
		shadowMap = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		lightCam = new PerspectiveCamera(67, shadowMap.getWidth(), shadowMap.getHeight());
		lightCam.position.set(-10, 5, 16);
		lightCam.lookAt(16, 0, 16);
		lightCam.update();

		shadowGenShader = new ShaderProgram(Gdx.files.internal("data/shaders/shadowgen.vert").readString(), Gdx.files
				.internal("data/shaders/shadowgen.frag").readString());
		if (!shadowGenShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shadow gen shader: " + shadowGenShader.getLog());

		shadowMapShader = new ShaderProgram(Gdx.files.internal("data/shaders/shadowmap.vert").readString(), Gdx.files
				.internal("data/shaders/shadowmap.frag").readString());
		if (!shadowMapShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shadow map shader: " + shadowMapShader.getLog());

	}
	public Renderer() {
		initiateShadows();
		sb = new SpriteBatch();

		InputStream in = Gdx.files.internal("data/SkySphere2.obj").read();
		skysphere = ObjLoader.loadObj(in);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		skysphereShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/skysphereShader.vert").readString(), Gdx.files.internal(
		"data/shaders/skysphereShader.frag").readString());
		if (!skysphereShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ skysphereShader.getLog());

		cubeTexture = new Texture(Gdx.files.internal("data/grassmap.png"));
		lightTexture = new Texture(Gdx.files.internal("data/light.png"));
		//texture av Remus tagen 2012-07-16 m�ste ge credit om ska anv�ndas
		//http://forums.epicgames.com/threads/603122-Remus-high-resolution-skydome-texture-pack
		skysphereTexture = new Texture(Gdx.files.internal("data/skydome.bmp"));
	}
	public void render(Application app) {
		app.cam.update();
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);

		renderSkySphere(app);
		renderMapChunks(app);
		renderLights(app);
		if(app.multiplayer){
			renderMultiplayer(app);
		}
		renderVector(app.from,app.to,app);
		//renderCharacter(app);
		for (int i = 0; i < app.map.chunks.size;i++) {
			if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0) {
				this.renderBoundingBox(app,app.map.chunks.get(i).bounds);
			}
		}
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		sb.begin();
		app.ch.inventory.get(app.ch.weapon).render(sb);
		sb.end();
	}

	public void renderSkySphere(Application app){
		skysphereTexture.bind(0);
		skysphereShader.begin();

		skysphereShader.setUniformi("s_texture", 0);

		skysphereModel.setToTranslation(app.cam.position.x,app.cam.position.y+20,app.cam.position.z);

		skysphereModel.scl(1.5f);
		skysphereModel.rotate(0, 0,1 , 180);
		modelViewProjectionMatrix.set(app.cam.combined);
		modelViewProjectionMatrix.mul(skysphereModel);	

		skysphereShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		skysphere.render(skysphereShader, GL20.GL_TRIANGLES);
		skysphereShader.end();
	}

	public void renderCharacter(Application app) {
		lightTexture.bind(0);
		simpleShader.begin();
		simpleShader.setUniformi("s_texture", 0);
		modelViewProjectionMatrix.set(app.cam.combined);
		modelViewProjectionMatrix.mul(app.ch.modelMatrix);
		simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
		app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
		simpleShader.end();
	}

	public void renderMultiplayer(Application app) {
		for(int i = 0; i< app.players.length; i++){
			if(app.clientid != i)
			if(app.players[i] != null){
				skysphereTexture.bind(0);
				simpleShader.begin();
				simpleShader.setUniform4fv("scene_light", app.light.color, 0, 4);
				simpleShader.setUniformf("scene_ambient_light", 0.2f,0.2f,0.2f, 1.0f);
				simpleShader.setUniformi("s_texture", 0);
				cubeModel.setToTranslation(app.players[i].posX,app.players[i].posY,app.players[i].posZ);

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
				//				simpleShader.setUniformf("dir_light",0f,1f,0f);

				app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
			}
		}
	}
	public void renderMapChunks(Application app) {
		
		boolean onlyShadowmap;
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {
			renderMode = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.N)) {
			renderMode = 2;
		} else if (Gdx.input.isKeyPressed(Input.Keys.B)) {
			renderMode = 3;
		} else if (Gdx.input.isKeyPressed(Input.Keys.V)) {
			renderMode = 4;
		}
		if (renderMode == 2) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                    
			//Gdx.gl.glEnable(GL20.GL_CULL_FACE);
			//Gdx.gl.glCullFace(GL20.GL_FRONT);
			Gdx.gl.glDisable(GL20.GL_CULL_FACE);
			shadowMap.begin();
			shadowGenShader.begin();
			for (int i = 0; i < app.map.chunks.size;i++) {
				if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
					cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);
					modelViewProjectionMatrix.set(lightCam.combined);
					modelViewProjectionMatrix.mul(cubeModel);
					shadowGenShader.setUniformMatrix("u_projTrans", modelViewProjectionMatrix);
					app.map.chunks.get(i).chunkMesh.render(shadowGenShader, GL20.GL_TRIANGLES);
				}
			}			
			shadowGenShader.end();
			shadowMap.end();

			Gdx.gl.glDisable(GL20.GL_CULL_FACE);

			shadowMapShader.begin();
			for (int i = 0; i < app.map.chunks.size;i++) {
				if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
					shadowMap.getColorBufferTexture().bind(0);
					shadowMapShader.setUniformi("s_shadowMap", 0);
					cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);
					modelViewProjectionMatrix.set(app.cam.combined);
				//	modelViewProjectionMatrix.mul(cubeModel);
					shadowMapShader.setUniformMatrix("u_projTrans", modelViewProjectionMatrix);

					modelViewProjectionMatrix.set(lightCam.combined);
				//	modelViewProjectionMatrix.mul(cubeModel);
					shadowMapShader.setUniformMatrix("u_lightProjTrans",modelViewProjectionMatrix);
					shadowMapShader.setUniformf("u_color", 1, 0, 0, 1);

					app.map.chunks.get(i).chunkMesh.render(shadowMapShader, GL20.GL_TRIANGLES);
				}
			}
			shadowMapShader.end();
		} else if (renderMode == 3) {
			app.cam.position.set(lightCam.position);
			app.cam.direction.set(lightCam.direction);
			app.cam.up.set(lightCam.up);
			app.cam.update();
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                    
//			Gdx.gl.glEnable(GL20.GL_CULL_FACE);
//			Gdx.gl.glCullFace(GL20.GL_FRONT);
			Gdx.gl.glDisable(GL20.GL_CULL_FACE);
			shadowGenShader.begin();
			for (int i = 0; i < app.map.chunks.size;i++) {
				if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
					cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);
					modelViewProjectionMatrix.set(lightCam.combined);
					modelViewProjectionMatrix.mul(cubeModel);
					shadowGenShader.setUniformMatrix("u_projTrans", modelViewProjectionMatrix);
					app.map.chunks.get(i).chunkMesh.render(shadowGenShader, GL20.GL_TRIANGLES);
				}
			}			
			shadowGenShader.end();
		} else if (renderMode == 4) {
			app.cam.position.set(lightCam.position);
			app.cam.direction.set(lightCam.direction);
			app.cam.up.set(lightCam.up);
			app.cam.update();
			cubeTexture.bind(0);
			simpleShader.begin();
			simpleShader.setUniform4fv("scene_light", app.light.color, 0, 4);
			simpleShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
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


					//simpleShader.setUniformf("dir_light",0,0,0);

					app.map.chunks.get(i).chunkMesh.render(simpleShader, GL20.GL_TRIANGLES);
					vertices+=app.map.chunks.get(i).chunkMesh.getNumVertices();
				}
			}
			//System.out.println("Vertices: " + vertices);
			simpleShader.end();
		} else {
			cubeTexture.bind(0);
			simpleShader.begin();
			simpleShader.setUniform4fv("scene_light", app.light.color, 0, 4);
			simpleShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
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


					//simpleShader.setUniformf("dir_light",0,0,0);

					app.map.chunks.get(i).chunkMesh.render(simpleShader, GL20.GL_TRIANGLES);
					vertices+=app.map.chunks.get(i).chunkMesh.getNumVertices();
				}
			}
			//System.out.println("Vertices: " + vertices);
			simpleShader.end();
		}
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
