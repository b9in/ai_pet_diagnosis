# AI 반려동물 안구 자가진단 어플리케이션
AI 반려동물 안구 자가진단 안드로이드 어플리케이션

## 프로젝트 소개
프로젝트 참여 인원 : 3명 (안드로이드, 백엔드, 인공지능)

이 프로젝트는 사용자가 반려동물의 안구를 업로드하여 YOLO 모델을 통해 AI가 안구 질환을 판별해주는 안드로이드 어플리케이션입니다. Google Maps, Places API를 사용하여 현재 위치를 불러오고 주변 동물병원이 표시됩니다. 이 프로젝트에서 안드로이드 개발을 담당하였습니다.

## 주요 기능

사용자의 간편한 화면 전환을 위해 NavigationBarView를 활용해 하단 메뉴를 표시하였습니다.

하단 메뉴는 홈 / 병원 / 사전 메뉴로 구성되어 있습니다.

#### 홈 화면
<img src="https://github.com/b9in/ai_pet_diagnosis/assets/128045612/4203862c-4cb7-4fe4-b776-576acdd90787" width="300" height="600"/>

홈 화면에는 사용자의 프로필과 프로필을 수정할 수 있는 profile 버튼이 있고 ViewPager를 통해 사용자가 자주 묻는 질문을 스와이프하여 볼 수 있도록 하였습니다.

반려동물 안구 자가진단 버튼을 통해 진단 페이지로 이동할 수 있습니다.

<img src="https://github.com/b9in/ai_pet_diagnosis/assets/128045612/0fb42b57-5403-46c8-a4d2-65e23bfcb60f" width="300" height="600"/>

진단 페이지에서는 사용자 기기의 카메라를 통해 사진을 찍거나 앨범을 통해 반려동물 안구 이미지를 업로드할 수 있습니다.

이미지를 업로드하고 SEND 버튼을 누르면 서버로 이미지가 전송되어 Yolo 모델 인공지능이 안구 질병을 판별합니다.

<img src="https://github.com/b9in/ai_pet_diagnosis/assets/128045612/2a4b4067-4c0c-4dbd-94a8-c0fad1b1c3bf" width="300" height="600"/>

판별을 끝낸 인공지능이 결과 값을 안드로이드에 전송하여 결과를 표시합니다.

가능성 높은 질환명과 질환 설명을 나타내고 주변 병원을 표시하는 맵 화면과 사전 화면으로 이동할 수 있는 버튼을 제공합니다.

<img src="https://github.com/b9in/ai_pet_diagnosis/assets/128045612/cf29a0bf-b41a-4586-a58f-bd0abf950b6c" width="300" height="110"/>

#### 맵 화면

<img src="https://github.com/b9in/ai_pet_diagnosis/assets/128045612/2a4b4067-4c0c-4dbd-94a8-c0fad1b1c3bf" width="300" height="600"/>

#### 사전 화면

## 느낀 점
