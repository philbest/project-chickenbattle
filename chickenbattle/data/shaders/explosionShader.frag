#ifdef GL_ES
precision mediump float;
#endif
varying float v_ptime;
void main()
{
    gl_FragColor =  vec4(0.8/(v_ptime/500),0.0,0.0,1.0); 
}