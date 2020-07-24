#version 450 core

layout (location = 0) in vec2 coords;

layout (location = 0) out vec4 pixelColor;

uniform sampler2D textureSampler;

void main(void) {
	pixelColor = texture(textureSampler, coords);
}