attribute vec4 a_position;
attribute vec4 a_pdir;
uniform mat4 u_mvpMatrix;
uniform float u_ptime;
varying float v_ptime;
void main()
{
v_ptime = u_ptime;
gl_Position = u_mvpMatrix * (a_position+a_pdir*u_ptime/300);
}