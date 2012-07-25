attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
attribute float a_occlusion;

uniform mat4 u_mvpMatrix;
uniform mat3 normalMatrix;
uniform mat4 u_modelViewMatrix;

varying vec3 N;
varying vec3 v;
varying vec2 v_texCoord;
void main()
{
	v_texCoord = a_texCoord0;
	v = vec3(u_modelViewMatrix * a_position);
	N = normalize(normalMatrix * a_normal);
	gl_Position = u_mvpMatrix * a_position;
}