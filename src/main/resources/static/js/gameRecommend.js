$(document).ready(function() {
    $('input[name="genre"], input[name="tag"]').change(function() {
        var selectedGenres = [];
        $('input[name="genre"]:checked').each(function() {
            selectedGenres.push($(this).val());
            console.log(selectedGenres);
        });

        var selectedTags = [];
        $('input[name="tag"]:checked').each(function() {
            selectedTags.push($(this).val());

        });
        var gtParams = {"genres": selectedGenres,
                        "tags": selectedTags,
                        };
        $.ajax({
            url: "recommend/search",
            type: "POST",
            data: gtParams,
            datatype: "json",
            contentType :   "application/x-www-form-urlencoded; charset=UTF-8",
            success: function(data) {
                var gamesContainer = $("#gamesContainer");
                gamesContainer.empty();

                $.each(data, function(index, game) {

                    var image = '<img src="/img/' + game + '.jpg" alt="' + game + '" class="game-image">';
                    var name = game;
                    // 이미지가 로드되면 추가하기

                    $(image).on('load', function() {
                        var aTag = $('<a href="/showgame?name='+ name +'"></a>');
                        aTag.append(image);
                        gamesContainer.append(aTag);

                    }).on('error', function() {
                        // 이미지 로드 에러 처리
                        console.log('이미지 로드 에러:', game.name);
                    });
                });
            }
        });
    });
});