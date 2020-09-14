#version 450 core

in vec2 coords;

out vec4 pixelColor;

uniform sampler2D textureSampler;
uniform vec3 textColor;

void main(void) {
	pixelColor = vec4(textColor, texture(textureSampler, coords).a);
}