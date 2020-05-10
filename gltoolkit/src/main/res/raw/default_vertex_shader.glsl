uniform mat4 u_MVPMatrix;
uniform mat4 u_STMatrix;
attribute vec4 a_Position;
attribute vec4 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
void main() {
  gl_Position = u_MVPMatrix * a_Position;
  v_TextureCoordinates = (u_STMatrix * a_TextureCoordinates).xy;
}