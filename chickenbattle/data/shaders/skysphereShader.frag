#ifdef GL_ES
precision lowp float;
#endif

uniform sampler2D s_texture;
varying vec2 v_texCoord;


void main()
{
	vec4 color;
	color = texture2D( s_texture, v_texCoord );

	gl_FragColor = color;
}