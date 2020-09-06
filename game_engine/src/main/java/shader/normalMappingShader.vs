#version 450 core

in vec4 position;
in vec2 texturedCoords;
in vec3 normals;
in vec3 vertexTangents;

out vec2 coords;
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
uniform vec4 clipPlane;

const float fogDensity = 0.03;
const float forGradient = 1.5;

void main() {

	vec4 worldPosition = transformation * position;
	vec4 positionToCamera = view * worldPosition;
	
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projection * positionToCamera;
	
	coords = vec2(texturedCoords.x / textureAmount.x, texturedCoords.y / textureAmount.y) + textureOffset;
	//tangents = vertexTangents;
	
	vec3 actualNormal = normals;
	if(useFakeLight > 0.5) {
		actualNormal = vec3(0, 1, 0);
	}
	
	vec3 surfaceNormal = (view * transformation * vec4(actualNormal, 0)).xyz;
	
	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((view * vec4(vertexTangents, 0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
	
	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);
	
	for(int i = 0; i < 4; i++) toLightVector[i] = toTangentSpace * lightPosition[i] - positionToCamera.xyz;
	toCameraVector = toTangentSpace * -positionToCamera.xyz;
	
	float distance = length(positionToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), forGradient));
	visibility = clamp(visibility, 0, 1);
}