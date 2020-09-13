#version 450 core

in vec2 coords;

out vec4 pixelColor;

uniform sampler2D textureSampler;

void main(void) {
	pixelColor = texture(textureSampler, coords);
}