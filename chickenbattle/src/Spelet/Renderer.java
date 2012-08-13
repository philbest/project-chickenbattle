package Spelet;



import java.io.IOException;
import java.io.InputStream;

import network.Player;

import Map.Map;
import Particles.Particle;
import Particles.ParticleSystem;
import Screens.Application;
import Screens.Lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;


public class Renderer {
	public Mesh cube;
	Mesh skysphere;
	Matrix4 cubeModel = new Matrix4();
	Matrix4 cubeModel2 = new Matrix4();
	Matrix4 skysphereModel = new Matrix4();


	Matrix4 modelViewProjectionMatrix = new Matrix4();
	Matrix4 modelViewMatrix = new Matrix4();
	Matrix3 normalMatrix = new Matrix3();

	Texture cubeTexture,crackTexture, explosionBlockTexture;
	Texture lightTexture;
	Texture skysphereTexture;
	Texture grassTexture;

	ShaderProgram simpleShader;
	ShaderProgram charShader;
	ShaderProgram lineShader;
	ShaderProgram skysphereShader;
	ShaderProgram explosionShader;
	ShaderProgram billboardShader;
	ShaderProgram grassShader;
	String playerscore;
	SpriteBatch sb;
	Sprite score, teamscore;

	DecalBatch decalbatch;
	Decal grassbb;
	GroupStrategy strategy;
	BitmapFont font;

	FrameBuffer shadowMap;
	ShaderProgram shadowGenShader;
	ShaderProgram shadowMapShader;
	int renderMode;
	Texture red, blue, blood, normal;
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
	public Renderer(Application app) {
		initiateShadows();

		score = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/score.png")));
		teamscore = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/teamscore.png")));
		red = new Texture(Gdx.files.internal("data/red.png"));
		blue = new Texture(Gdx.files.internal("data/blue.png"));
		blood = new Texture(Gdx.files.internal("data/blood.png"));
		normal = new Texture(Gdx.files.internal("data/grassmap.png"));
		sb = new SpriteBatch();
		font = new BitmapFont();


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


		grassShader = new ShaderProgram(Gdx.files.internal("data/shaders/grassShader.vert").readString(), Gdx.files
				.internal("data/shaders/grassShader.frag").readString());
		if (!grassShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shadow gen shader: " + shadowGenShader.getLog());


		charShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/simpleChar.vert").readString(), Gdx.files.internal(
		"data/shaders/simpleChar.frag").readString());
		if (!charShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile simple shader: "
					+ charShader.getLog());

		explosionShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/explosionShader.vert").readString(), Gdx.files.internal(
		"data/shaders/explosionShader.frag").readString());
		if (!explosionShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ explosionShader.getLog());

		billboardShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/billboardShader.vert").readString(), Gdx.files.internal(
		"data/shaders/billboardShader.frag").readString());
		if (!billboardShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ billboardShader.getLog());


		lineShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/lineShader.vert").readString(), Gdx.files.internal(
		"data/shaders/lineShader.frag").readString());
		if (!lineShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ lineShader.getLog());


		skysphereShader = new ShaderProgram(Gdx.files.internal(
		"data/shaders/skysphereShader.vert").readString(), Gdx.files.internal(
		"data/shaders/skysphereShader.frag").readString());
		if (!skysphereShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile shader: "
					+ skysphereShader.getLog());

		cubeTexture = new Texture(Gdx.files.internal("data/blockmap.png"));
		explosionBlockTexture = new Texture(Gdx.files.internal("data/explosionblock.png"));
		crackTexture = new Texture(Gdx.files.internal("data/cracks.png"));
		lightTexture = new Texture(Gdx.files.internal("data/light.png"));
		grassTexture = new Texture(Gdx.files.internal("data/grassbb.png"));
		blood = new Texture(Gdx.files.internal("data/blood.png"));
		//texture av Remus tagen 2012-07-16 m�ste ge credit om ska anv�ndas
		//http://forums.epicgames.com/threads/603122-Remus-high-resolution-skydome-texture-pack
		skysphereTexture = new Texture(Gdx.files.internal("data/skydome.bmp"));
		// Load a Texture
		Texture image = new Texture(Gdx.files.internal("data/grassbb.png"));
		// create a decal sprite
		grassbb = Decal.newDecal(32, 32, new TextureRegion(image), true);

		// create a DecalBatch to render them with just once at startup
		//		decalbatch = new DecalBatch();
		//		decalbatch.
		//		grassbb.setPosition(500, 250, 5);

	}

	public void render(Application app) {
		app.cam.update();
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);

		renderMapChunks(app);

		if(app.multiplayer){
			renderMultiplayer(app);
		}
		//		renderVector(app.from,app.to,app);
		//		for (int i = 0; i < app.map.chunks.size;i++) {
		//			if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0) {
		//				this.renderBoundingBox(app,app.map.chunks.get(i).bounds);
		//			}
		//		}
		renderSkySphere(app);
		renderExplosions(app);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		sb.begin();
		app.ch.inventory.get(app.ch.weapon).render(sb);
		app.gi.render(sb);
		if(app.scoreboard && (((Lobby) app.main.screens.get(Main.LOBBY)).gs.mode == StaticVariables.teamServer)){
			teamscore.setPosition(Gdx.graphics.getWidth()/2-score.getWidth()/2, 100);
			teamscore.draw(sb,0.80f);
			int red = 0, blue = 0;
			for(int i =0; i < app.players.length; i++){	
				Player x = app.players[i];
				if(x != null && x.currentTeam == StaticVariables.teamBlue){
					playerscore = x.name +" kills : " +x.kills + ". Deaths " + x.deaths + ". Ping: " + app.ping;
					float textWidth = font.getBounds(playerscore).width;
					float textHeight = font.getBounds(playerscore).height;
					font.setColor(Color.BLUE);
					font.draw(sb, playerscore, Gdx.graphics.getWidth()/2 - textWidth/2 + 140, 400 - (blue*20) + textHeight / 2);
					blue++;
				} else if(x != null && x.currentTeam == StaticVariables.teamRed){
					playerscore = x.name +" kills : " +x.kills + ". Deaths " + x.deaths + ". Ping: " + app.ping;
					float textWidth = font.getBounds(playerscore).width;
					float textHeight = font.getBounds(playerscore).height;
					font.setColor(Color.RED);
					font.draw(sb, playerscore, Gdx.graphics.getWidth()/2 - textWidth/2 - 140, 400 - (red*20) + textHeight / 2);
					red++;
				}
			}

		} else if(app.scoreboard && (((Lobby) app.main.screens.get(Main.LOBBY)).gs.mode == StaticVariables.freeforall)){
			score.setPosition(Gdx.graphics.getWidth()/2-score.getWidth()/2, 100);
			score.draw(sb,0.80f);
			for(int i =0; i < app.players.length; i++){	
				Player x = app.players[i];
				if(x != null){
					playerscore = x.name +" kills : " +x.kills + ". Deaths " + x.deaths + ". Ping: " + app.ping;
					float textWidth = font.getBounds(playerscore).width;
					float textHeight = font.getBounds(playerscore).height;
					font.draw(sb, playerscore, Gdx.graphics.getWidth()/2 - textWidth/2, 400 - (i*20) + textHeight / 2);
				}
			}
		}	
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
	public void renderExplosions(Application app) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		explosionBlockTexture.bind(0);
		explosionShader.begin();
		explosionShader.setUniform4fv("scene_light", app.light.color, 0, 4);
		explosionShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
		explosionShader.setUniformi("s_texture", 0);
		explosionShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
		explosionShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
		explosionShader.setUniformf("material_shininess", 0.5f);
		simpleShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);

		for (int i = 0; i < app.explosions.explosions.size; i++) {
			Explosion e = app.explosions.explosions.get(i);
			modelViewProjectionMatrix.set(app.cam.combined);
			modelViewProjectionMatrix.mul(e.mat);
			modelViewMatrix.set(app.cam.view);
			modelViewMatrix.mul(e.mat);
			normalMatrix.set(modelViewMatrix);
			explosionShader.setUniformMatrix("normalMatrix", normalMatrix);
			explosionShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
			explosionShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
			explosionShader.setUniformf("u_alpha",e.alpha);
			app.explosions.parts.get(1).render(explosionShader, GL20.GL_TRIANGLES);
		}

		explosionShader.end();


		Gdx.gl.glDepthMask(false);

		ParticleSystem.midExplo.bind(0);
		billboardShader.begin();
		billboardShader.setUniformi("s_texture", 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		for (int i = 0; i < app.explosionParticles.particles.size; i++) {
			Particle e = app.explosionParticles.particles.get(i);
			if (e.isSmoke) { 
				billboardShader.setUniform4fv("u_colorTint", e.colorTint, 0, 4);
				billboardShader.setUniformi("u_texVal",e.texVal);
				modelViewProjectionMatrix.set(app.cam.combined);
				modelViewProjectionMatrix.mul(e.modelMatrix);
				billboardShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
				ParticleSystem.quad.render(billboardShader, GL20.GL_TRIANGLES);
			}
		}
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (int i = 0; i < app.explosionParticles.particles.size; i++) {
			Particle e = app.explosionParticles.particles.get(i);
			if (!e.isSmoke) { 
				billboardShader.setUniform4fv("u_colorTint", e.colorTint, 0, 4);
				billboardShader.setUniformi("u_texVal",e.texVal);
				modelViewProjectionMatrix.set(app.cam.combined);
				modelViewProjectionMatrix.mul(e.modelMatrix);
				billboardShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
				ParticleSystem.quad.render(billboardShader, GL20.GL_TRIANGLES);
			}
		}
		billboardShader.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
	}
	public void renderMultiplayer(Application app) {

		for(int i = 0; i< app.players.length; i++){
			if(app.clientid != i)
				if(app.players[i] != null){

					charShader.begin();
					try{
						if(app.players[i].currentTeam == StaticVariables.teamBlue){
							StaticAnimations.walk.parts.get(4).setTexture(blue);
						}
						else if(app.players[i].currentTeam == StaticVariables.teamRed){
							StaticAnimations.walk.parts.get(4).setTexture(red);
						}
						else if(app.players[i].currentTeam == StaticVariables.allTeam){
							StaticAnimations.walk.parts.get(4).setTexture(normal);
						}

						if(app.players[i].hit){
							StaticAnimations.walk.parts.get(6).setTexture(blood);
							app.bloodTimer = 1000;
						}
						else if(app.bloodTimer < 0){
							StaticAnimations.walk.parts.get(6).setTexture(normal);
						}

					}
					catch(NullPointerException e){
						e.getStackTrace();
						System.out.println("null");
					}

					charShader.setUniform4fv("scene_light", app.light.color, 0, 4);
					charShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
					charShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
					charShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
					charShader.setUniformf("material_shininess", 0.5f);
					charShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);				
					StaticAnimations.walk.render(app, app.players[i]);
					this.renderBoundingBox(app,app.players[i].box);
					charShader.end();
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
			crackTexture.bind(1);
			simpleShader.begin();
			simpleShader.setUniform4fv("scene_light", app.light.color, 0, 4);
			simpleShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
			simpleShader.setUniformi("s_texture", 0);
			simpleShader.setUniformi("s_crackTexture", 1);
			simpleShader.setUniformf("material_diffuse", 1f,1f,1f, 1f);
			simpleShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
			simpleShader.setUniformf("material_shininess", 0.5f);
			simpleShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);
			//			int vertices = 0;
			for (int i = 0; i < app.map.chunks.size;i++) {
				if (app.map.chunks.get(i).chunkMesh != null && app.map.chunks.get(i).chunkMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
					cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);

					modelViewProjectionMatrix.set(app.cam.combined);
					modelViewProjectionMatrix.mul(cubeModel);
					modelViewMatrix.set(app.cam.view);
					modelViewMatrix.mul(cubeModel);
					normalMatrix.set(modelViewMatrix);
					simpleShader.setUniformMatrix("normalMatrix", normalMatrix);
					simpleShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
					simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

					//simpleShader.setUniformf("dir_light",0,0,0);

					app.map.chunks.get(i).chunkMesh.render(simpleShader, GL20.GL_TRIANGLES);	
					//					vertices+=app.map.chunks.get(i).chunkMesh.getNumVertices();
				}
			}

			simpleShader.end();
			Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
			Gdx.gl20.glEnable(GL20.GL_BLEND);

			grassShader.begin();
			grassShader.setUniform4fv("scene_light", app.light.color, 0, 4);
			grassShader.setUniformf("scene_ambient_light", 0.3f,0.3f,0.3f, 1.0f);
			grassTexture.bind(0);
			grassShader.setUniformf("material_diffuse", 0.75f,0.75f,0.75f, 1f);
			grassShader.setUniformf("material_specular", 0.0f,0.0f,0.0f, 1f);
			grassShader.setUniformf("material_shininess", 0.5f);
			grassShader.setUniform3fv("u_lightPos",app.light.getViewSpacePositions(app.cam.view), 0,3);
			simpleShader.setUniformi("s_texture", 0);
			for (int i = 0; i < app.map.chunks.size;i++) {
				if (app.map.chunks.get(i).grassMesh != null && app.map.chunks.get(i).grassMesh.getNumVertices() > 0 && app.cam.frustum.boundsInFrustum(app.map.chunks.get(i).bounds)) {
					cubeModel.setToTranslation(app.map.chunks.get(i).x*Map.chunkSize,app.map.chunks.get(i).y*Map.chunkSize,app.map.chunks.get(i).z*Map.chunkSize);

					modelViewProjectionMatrix.set(app.cam.combined);
					modelViewProjectionMatrix.mul(cubeModel);
					modelViewMatrix.set(app.cam.view);
					modelViewMatrix.mul(cubeModel);
					normalMatrix.set(modelViewMatrix);
					grassShader.setUniformMatrix("normalMatrix", normalMatrix);
					grassShader.setUniformMatrix("u_modelViewMatrix", modelViewMatrix);
					grassShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);


					//simpleShader.setUniformf("dir_light",0,0,0);
					app.map.chunks.get(i).grassMesh.render(grassShader, GL20.GL_TRIANGLES);				
				}
			}
			Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			//System.out.println("Vertices: " + vertices);
			grassShader.end();
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
		lineShader.begin(); 
		vectorTest.setVertices(vertices);
		vectorTest.setIndices(indices);

		modelViewProjectionMatrix.set(app.cam.combined);
		lineShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		vectorTest.render(lineShader, GL20.GL_LINE_STRIP);
		lineShader.end();
		vectorTest.dispose();
	}

	//	public void renderLights(Application app) {
	//		lightTexture.bind(0);
	//		simpleShader.begin();
	//		simpleShader.setUniformi("s_texture", 0);
	//		cubeModel.setToTranslation(app.light.posX,app.light.posY,app.light.posZ);
	//		modelViewProjectionMatrix.set(app.cam.combined);
	//		modelViewProjectionMatrix.mul(cubeModel);
	//		simpleShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
	//		app.cube.cubeMesh.render(simpleShader, GL20.GL_TRIANGLES);
	//		simpleShader.end();
	//	}

	public void renderVector(Vector3 from,Vector3 to, Application app) {
		Mesh vectorTest = new Mesh(true,2,0,new VertexAttribute(Usage.Position, 3,"a_position"));
		lineShader.begin();
		float[] vertices = new float[]{ from.x,from.y,from.z,to.x,to.y,to.z}; 
		vectorTest.setVertices(vertices);


		Matrix4 modelViewProjectionMatrix = new Matrix4();
		modelViewProjectionMatrix.set(app.cam.combined);

		lineShader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);

		vectorTest.render(lineShader, GL20.GL_LINES);
		lineShader.end();
		vectorTest.dispose();
	}
}
