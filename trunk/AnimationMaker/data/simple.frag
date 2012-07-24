#ifdef GL_ES
precision lowp float;
#endif

uniform sampler2D s_texture;
uniform int s_hit;
varying vec2 v_texCoord;
void main()
{
	vec4 color;
	color = texture2D( s_texture, v_texCoord );
	gl_FragColor = color+s_hit*vec4(0.5,0.0,0.0,1.0);
}