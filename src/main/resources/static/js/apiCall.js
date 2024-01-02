// function callApi(commentId) {
//   var apiUrl = "localhost:8080/api/comments" + commentId;
//   var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlVTRVIiLCJleHAiOjE3MDQwMTI4NTEsImlhdCI6MTcwNDAxMTA1MX0.3SSrUzuXSX7W4OroBMCwJJAV9i2R3W6ZNWVP42QQTjM"
//   fetch(apiUrl, {
//     headers: {
//       'Authorization': 'Bearer' + token
//     }
//   })
//   .then(response => {
//     if (response.ok) {
//       return response.json();
//     }
//     // throw new Error() do something 해야함
//   })
//   .then(date => {
//     var commentData = data.data;
//     document.getElementById("getComment").innerText = JSON.stringify(
//         commentData)
//   })
// }