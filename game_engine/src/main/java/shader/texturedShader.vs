#version 450 core

in vec4 position;
in vec2 texturedCoords;
in vec3 normal;

out vec2 coords;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition[4];
uniform float useFakeLight;
uniform vec2 textureAmount;
uniform vec2 textureOffset;

const float fogDensity = 0.03;
const float forGradient = 1.5;

void main() {
	vec4 worldPosition = transformation * position;
	vec4 positionToCamera = view * worldPosition;
	gl_Position = projection * positionToCamera;
	coords = vec2(texturedCoords.x / textureAmount.x, texturedCoords.y / textureAmount.y) + textureOffset;
	
	vec3 actualNormal = normal;
	if(useFakeLight > 0.5) {
		actualNormal = vec3(0, 1, 0);
	}
	
	surfaceNormal = (transformation * vec4(actualNormal, 0)).xyz;
	for(int i = 0; i < 4; i++) toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	toCameraVector = (inverse(view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
	
	float distance = length(positionToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), forGradient));
	visibility = clamp(visibility, 0, 1);
}