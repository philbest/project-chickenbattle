#ifdef GL_ES
precision lowp float;
#endif

uniform sampler2D s_texture;
uniform vec4 u_colorTint;
uniform int u_texVal;
varying vec2 v_texCoord;
void main()
{
	vec2 uv = v_texCoord;
	float u = (u_texVal % 4) / 4;
	float f_texVal = u_texVal;
	float v = floor(f_texVal/4)/4;
	uv.x = uv.x + u;
	uv.y = uv.y + v;
	vec4 color = texture2D( s_texture, uv );
	color.a = color.r;
	color = color * u_colorTint;
	gl_FragColor = color;
}