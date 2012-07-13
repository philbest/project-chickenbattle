attribute vec4 a_position;
attribute float a_ttl;
uniform mat4 u_mvpMatrix;
varying float v_ttl;
void main()
{
gl_Position = u_mvpMatrix * a_position;
v_ttl = a_ttl;
gl_PointSize = 10.0;  
}