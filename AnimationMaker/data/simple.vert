attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_mvpMatrix;
varying vec2 v_texCoord;
void main()
{
	v_texCoord = a_texCoord0;
	gl_Position = u_mvpMatrix * a_position;
}