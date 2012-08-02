attribute vec4 a_position;
attribute vec4 a_pdir;
uniform mat4 u_mvpMatrix;
uniform float u_ptime;
void main()
{
gl_Position = u_mvpMatrix * a_position*a_pdir*u_ptime;
}